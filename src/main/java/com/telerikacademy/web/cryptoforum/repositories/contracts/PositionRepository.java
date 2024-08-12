package com.telerikacademy.web.cryptoforum.repositories.contracts;

import com.telerikacademy.web.cryptoforum.models.Position;

import java.util.List;

public interface PositionRepository {

    Position getPositionById(int id);
}
