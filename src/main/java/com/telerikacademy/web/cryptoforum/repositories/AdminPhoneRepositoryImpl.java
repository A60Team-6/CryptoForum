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
}
