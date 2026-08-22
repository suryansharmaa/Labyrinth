package com.projects.labyrinth.controller;

import com.projects.labyrinth.dto.CreateGameRoomDto;
import com.projects.labyrinth.service.GameRoomService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rooms")
@AllArgsConstructor
public class GameRoomController {
    private final GameRoomService gameRoomService;

    @PostMapping
    public ResponseEntity<String> createRoom(@RequestBody CreateGameRoomDto createGameRoomDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gameRoomService.createRoom(createGameRoomDto));
    }
}
