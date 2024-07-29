package com.telerikacademy.web.cryptoforum.PositionServiceImplTests;

import com.telerikacademy.web.cryptoforum.HelperClass;
import com.telerikacademy.web.cryptoforum.models.Position;
import com.telerikacademy.web.cryptoforum.models.User;
import com.telerikacademy.web.cryptoforum.repositories.contracts.PositionRepository;
import com.telerikacademy.web.cryptoforum.services.PositionServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class PositionServiceImplTests {


    @Mock
    PositionRepository mockRepository;

    @InjectMocks
    PositionServiceImpl positionService;

    @Test
    public void get_Should_Return_User_WhenProperIdIsProvided(){

        Mockito.when(mockRepository.getPositionById(1)).thenReturn(new Position(1, "admin"));

        Position actualPosition = positionService.getPositionById(1);

        Assertions.assertEquals(1, actualPosition.getId());
    }
}
