package com.telerikacademy.web.cryptoforum.repositories.contracts;

import com.telerikacademy.web.cryptoforum.models.Position;

public interface PositionRepository {

    Position getPositionById(int id);
}
