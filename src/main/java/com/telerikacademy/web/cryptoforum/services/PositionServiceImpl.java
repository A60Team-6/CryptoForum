package com.telerikacademy.web.cryptoforum.services;

import com.telerikacademy.web.cryptoforum.models.Position;
import com.telerikacademy.web.cryptoforum.repositories.contracts.PositionRepository;
import com.telerikacademy.web.cryptoforum.services.contracts.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;

    @Autowired
    public PositionServiceImpl(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    @Override
    public Position getPositionById(int id) {
        return positionRepository.getPositionById(id);
    }

}
