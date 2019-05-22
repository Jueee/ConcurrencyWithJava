package com.app.jueee.concurrency.chapter09.example3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import com.app.jueee.concurrency.chapter09.common3.Person;
import com.app.jueee.concurrency.chapter09.common3.PersonPair;

public class CommonPersonMapper implements Function<Person, List<PersonPair>>{

    @Override
    public List<PersonPair> apply(Person person) {
        List<PersonPair> ret = new ArrayList<>();
        List<String> contacts = person.getContacts();
        Collections.sort(contacts);
        
        // 处理整个联系人列表，为每个联系人创建 PersonPair 对象。
        for(String contact: contacts) {
            PersonPair personExt = new PersonPair();
            if (person.getId().compareTo(contact) < 0) {
                personExt.setId(person.getId());
                personExt.setOtherId(contact);
            } else {
                personExt.setId(contact);
                personExt.setOtherId(person.getId());
            }
            
            // 将联系人列表添加到新对象，并且将该对象添加到结果列表。
            personExt.setContacts(contacts);
            ret.add(personExt);
        }
        
        return ret;
    }
}
