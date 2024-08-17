package com.telerikacademy.web.cryptoforum.repositories;

import com.telerikacademy.web.cryptoforum.exceptions.EntityNotFoundException;
import com.telerikacademy.web.cryptoforum.models.AdminPhone;
import com.telerikacademy.web.cryptoforum.repositories.contracts.AdminPhoneRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class AdminPhoneRepositoryImpl implements AdminPhoneRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public AdminPhoneRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public AdminPhone getPhoneNumber(String phoneNumber) {
        try (Session session = sessionFactory.openSession()) {
            Query<AdminPhone> query = session.createQuery("from AdminPhone where phoneNumber = :phoneNumber", AdminPhone.class);
            query.setParameter("phoneNumber", phoneNumber);

            List<AdminPhone> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Admin", "number", phoneNumber);
            }

            return result.getFirst();
        }
    }

    @Override
    public void addPhoneNumber(AdminPhone adminPhone) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(adminPhone);
            session.getTransaction().commit();
        }
    }

    @Override
    public AdminPhone getPhoneNumberById(int phoneId){
        try (Session session = sessionFactory.openSession()) {
            AdminPhone adminPhone = session.get(AdminPhone.class, phoneId);
            if (adminPhone == null){
                throw new EntityNotFoundException("Phone", phoneId);
            }
            return adminPhone;
        }
    }

    @Override
    public void removePhoneFromAdmin(AdminPhone adminPhone){
        if(adminPhone == null){
            throw new IllegalArgumentException("Admin phone cannot be null");
        }

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            if (!session.contains(adminPhone)){
                adminPhone = session.merge(adminPhone);
            }
            session.remove(adminPhone);
            session.getTransaction().commit();
        }catch (Exception e){
            throw new RuntimeException("Fail in deleting phone number: " + adminPhone.getPhoneNumber());
        }
    }


}
