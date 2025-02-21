package org.example.addressbook.model;

import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.Objects;

public final class AddressBookNoRecord {
    @Id
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String phone;
    private final LocalDate birthday;

    public AddressBookNoRecord(Long id, String firstName, String lastName, String phone, LocalDate birthday) { // удален @Id из @Id Long id
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.birthday = birthday;
    }

    @Id
    public Long id() {
        return id;
    }

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
    }

    public String phone() {
        return phone;
    }

    public LocalDate birthday() {
        return birthday;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (AddressBookNoRecord) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.firstName, that.firstName) &&
                Objects.equals(this.lastName, that.lastName) &&
                Objects.equals(this.phone, that.phone) &&
                Objects.equals(this.birthday, that.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, phone, birthday);
    }

    @Override
    public String toString() {
        return "AddressBook[" +
                "id=" + id + ", " +
                "firstName=" + firstName + ", " +
                "lastName=" + lastName + ", " +
                "phone=" + phone + ", " +
                "birthday=" + birthday + ']';
    }


}
