package com.app.jueee.concurrency.chapter09.example3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.app.jueee.concurrency.chapter09.common3.Person;
import com.app.jueee.concurrency.chapter09.common3.PersonPair;

public class ConcurrentSocialNetwork {
    
    /**
     *  接收社交网络上的人员列表（含有联系人），并且返回一个 PersonPair 对象列表，这些 PersonPair 对象中含有每一对互为联系人的用户之间的共同联系人。
     *	@param people
     *	@return
     */
    public static List<PersonPair> bidirectionalCommonContacts(List<Person> people) {
        ConcurrentMap<String, List<PersonPair>> group = people.parallelStream() // 创建流
                .map(new CommonPersonMapper())  // 将每个 Person 对象都转换到一个 PersonPair 对象列表中
                .flatMap(Collection::stream)    // 使用 flatMap() 方法将该流转换成一个  PersonPair 对象流。
                .collect(Collectors.groupingByConcurrent(PersonPair::getFullId));   // 使用 collect() 方法生成该 Map
        
        Collector<Collection<String>, AtomicReference<Collection<String>>, Collection<String>> intersecting = 
            Collector.of(()->new AtomicReference<>(null),       // 第一个参数是 Supplier 函数。需要创建一个中间数据结构时，总是要调用该 Supplier 。
                        (acc,list)-> {      // 第二个参数是 Accumulator 函数。该函数接收中间数据结构和一个输入值作为参数。
                            if(acc.get() == null) {     //  acc 参数是 AtomicReference ，而 list 参数是 ConcurrentLinkedDeque
                                acc.updateAndGet(value->new ConcurrentLinkedQueue<>(list));
                            } else {
                                acc.get().retainAll(list);
                            }
                        },
                        (acc1, acc2) -> {   // 第三个参数是 Combiner 函数。该函数只在并行流中调用，它接收两个中间数据结构作为参数，并且仅生成一个数据结构。
                            if(acc1.get() == null) {    // 如果其中一个参数为 null ，则返回另一个数据结构
                                return acc2;
                            }
                            if (acc2.get() == null) {
                                return acc1;
                            }
                            acc1.get().retainAll(acc2.get());
                            return acc1;
                        }, 
                        (acc) -> acc.get()== null ? Collections.emptySet() : acc.get(),     // 第四个参数是 Finisher 函数。该函数将最后的中间数据结构转换成我们希望返回的数据结构。
                        Collector.Characteristics.CONCURRENT,   // 最后一个参数指明该收集器是并发的
                        Collector.Characteristics.UNORDERED);
        
        List<PersonPair> personCommonContacts = group.entrySet()
                .parallelStream()
                .map((entry) -> {   // 使用 map() 方法将每个 PersonPair 对象列表转换为一个含有共同联系人的唯一 PersonPair 对象。
                        Collection<String> commonContacts = entry.getValue()
                                .parallelStream()
                                .map(p -> p.getContacts())
                                .collect(intersecting);
                        PersonPair pair = new PersonPair();
                        pair.setId(entry.getKey().split(",")[0]);
                        pair.setOtherId(entry.getKey().split(",")[1]);
                        pair.setContacts(new ArrayList<String>(commonContacts));
                        return pair;
                })
                .collect(Collectors.toList());
        
        return personCommonContacts;
    }
    
    
}
