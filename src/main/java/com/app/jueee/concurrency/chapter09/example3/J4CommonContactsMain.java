package com.app.jueee.concurrency.chapter09.example3;

import java.util.Date;
import java.util.List;

import com.app.jueee.concurrency.chapter09.common3.DataLoader;
import com.app.jueee.concurrency.chapter09.common3.Person;
import com.app.jueee.concurrency.chapter09.common3.PersonPair;

public class J4CommonContactsMain {

    public static void main(String[] args) {
        Date start, end;
        System.out.println("Concurrent Main Bidirectional - Test");
        List<Person> people = DataLoader.load("data//chapter09", "test.txt");
        start = new Date();
        List<PersonPair> peopleCommonContacts = ConcurrentSocialNetwork.bidirectionalCommonContacts(people);
        end = new Date();
        peopleCommonContacts.forEach(p -> System.out.println(p.getFullId() + ": " + getContacts(p.getContacts())));
        System.out.println("Execution Time: " + (end.getTime() - start.getTime()));
        System.out.println("Concurrent Main Bidirectional -  Facebook");
        people = DataLoader.load("data//chapter09", "facebook_contacts.txt");
        start = new Date();
        peopleCommonContacts = ConcurrentSocialNetwork.bidirectionalCommonContacts(people);
        end = new Date();
        peopleCommonContacts.forEach(p -> System.out.println(p.getFullId() + ": " + getContacts(p.getContacts())));
        System.out.println("Execution Time: " + (end.getTime() - start.getTime()));
    }

    private static String getContacts(List<String> contacts) {
        StringBuffer buffer = new StringBuffer();
        for (String contact : contacts) {
            buffer.append(contact + ",");
        }
        return buffer.toString();
    }
}
