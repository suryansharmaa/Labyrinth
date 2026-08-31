package com.projects.labyrinth.service;

import com.projects.labyrinth.dto.CreateGameRoomDto;
import com.projects.labyrinth.dto.GameRoomStateDto;
import com.projects.labyrinth.dto.JoinGameRoomDto;
import com.projects.labyrinth.dto.PlayerDto;
import com.projects.labyrinth.entity.GameRoom;
import com.projects.labyrinth.entity.Player;
import com.projects.labyrinth.repository.GameRoomRepository;
import com.projects.labyrinth.repository.PlayerRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Transactional
    public void joinRoom(String roomCode, JoinGameRoomDto joinGameRoomDto) {
        GameRoom room = gameRoomRepository.findByRoomCode(roomCode).orElseThrow(() -> new RuntimeException("Room Not Found"));

        if(!room.getStatus().equals("WAITING")) {
            throw new RuntimeException("Game already in progress");
        }

        Player player = new Player();
        player.setUserName(joinGameRoomDto.getUserName());
        player.setScore(0);
        player.setGameRoom(room);

        playerRepository.save(player);
    }

    @Transactional(readOnly = true)
    public GameRoomStateDto getRoomState(String roomCode) {
        GameRoom room = gameRoomRepository.findByRoomCode(roomCode).orElseThrow(() -> new RuntimeException("Room not found"));
        List<Player> players = playerRepository.findByGameRoomIdOrderByScoreDesc(room.getId());

        GameRoomStateDto gameRoomStateDto = new GameRoomStateDto();
        gameRoomStateDto.setRoomCode(room.getRoomCode());
        gameRoomStateDto.setStatus(room.getStatus());

        if (room.getCurrRiddle() != null) {
            gameRoomStateDto.setCurrQuestion(room.getCurrRiddle().getQuestion());
        }

        List<PlayerDto> leaderBoard = players.stream().map(player -> {
            PlayerDto playerDto = new PlayerDto();
            playerDto.setId(player.getId());
            playerDto.setUserName(player.getUserName());
            playerDto.setScore(player.getScore());
            return playerDto;
        }).toList();

        gameRoomStateDto.setLeaderBoard(leaderBoard);
        return gameRoomStateDto;
    }
}
