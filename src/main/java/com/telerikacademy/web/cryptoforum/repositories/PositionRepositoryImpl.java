package com.telerikacademy.web.cryptoforum.repositories;

import com.telerikacademy.web.cryptoforum.exceptions.EntityNotFoundException;
import com.telerikacademy.web.cryptoforum.models.Position;
import com.telerikacademy.web.cryptoforum.repositories.contracts.PositionRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PositionRepositoryImpl implements PositionRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public PositionRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Position getPositionById(int id) {
        try (Session session = sessionFactory.openSession()){
            Position position = session.get(Position.class, id);
            if(position == null){
                throw new EntityNotFoundException("Position", id);
            }
            return position;
        }
    }
}
