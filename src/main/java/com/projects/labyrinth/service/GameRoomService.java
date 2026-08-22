package com.projects.labyrinth.service;

import com.projects.labyrinth.dto.CreateGameRoomDto;
import com.projects.labyrinth.entity.GameRoom;
import com.projects.labyrinth.entity.Player;
import com.projects.labyrinth.repository.GameRoomRepository;
import com.projects.labyrinth.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class GameRoomService {
    private final GameRoomRepository gameRoomRepository;
    private final PlayerRepository playerRepository;

    public String createRoom(CreateGameRoomDto createGameRoomDto) {
        String roomCode = UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        GameRoom room = new GameRoom();
        room.setRoomCode(roomCode);
        room.setStatus("WAITING");
        gameRoomRepository.save(room);

        Player host = new Player();
        host.setUserName(createGameRoomDto.getHostUserName());
        host.setScore(0);
        host.setGameRoom(room);
        playerRepository.save(host);

        return roomCode;
    }
}
