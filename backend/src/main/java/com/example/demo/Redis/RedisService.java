package com.example.demo.Redis;

import com.example.demo.Dto.BoardDto;
import com.example.demo.Dto.RankDto;
import com.example.demo.Entity.Board;
import com.example.demo.Repository.BoardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private final BoardRepository boardRepository;

    public RedisService(RedisTemplate<String, String> redisTemplate, BoardRepository boardRepository){
        this.redisTemplate = redisTemplate;
        this.boardRepository = boardRepository;
    }

    public void addInToRankList(RankDto rankDto){
        String key = "rank";
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.add(key, rankDto.getValue(), rankDto.getScore());
    }

    public List<RankDto> getRankList() {
        String key = "rank";
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<String>> rankTypedTuples = zSetOperations.reverseRangeWithScores(key, 0, 2);

        List<RankDto> rankDtos = rankTypedTuples.stream().map(t -> new RankDto(t.getValue(), t.getScore())).collect(Collectors.toList());
        return rankDtos;
    }

    public void increaseViewByBoardTitle(String title){
        String key = "rank";
        redisTemplate.opsForZSet().incrementScore(key, title, 1);
    }

    public int getViewByBoardTitle(String boardTitle) {
        String key = "rank";
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        Double value = zSetOperations.score(key, boardTitle);

        if (value != null){
            return value.intValue();
        }

        return -1;
    }

//    @Scheduled(cron = "0/5 * * * * *")
    @Scheduled(cron = "0 0 0/1 * * *")
    public void InsertViewToDB(){
        Set<String> redisKeys = redisTemplate.keys("BoardView*");
        Iterator<String> it = redisKeys.iterator();
        while (it.hasNext()){
            String key = it.next();
            Long boardId = Long.valueOf(key.split("::")[1]);
            int view = Integer.parseInt(redisTemplate.opsForValue().get(key));
            boardRepository.updateView(boardId, view);
        }
    }

//    public void increaseViewByBoardId(Long boardId){
//        String key = "BoardView::" + boardId;
//        ValueOperations valueOperations = redisTemplate.opsForValue();
//        if (redisTemplate.hasKey(key)) {
//            valueOperations.increment(key);
//        }
//        else{
//            valueOperations.set(key, String.valueOf(boardRepository.findViewByBoardId(boardId)));
//            redisTemplate.expire(key, 1, TimeUnit.DAYS);
//        }
//    }

//    public int getViewByBoardId(Long boardId) {
//        String key = "BoardView::" + boardId;
//        int view = Integer.parseInt(redisTemplate.opsForValue().get(key));
//        return view;
//    }

    //    public boolean isFirstViewByEmail(String email, Long boardId) {
//        String key = "ViewHistory::" + email + "," + boardId;
//        if (redisTemplate.hasKey(key)) {
//            return false;
//        }
//        redisTemplate.opsForValue().set(key, "");
//        redisTemplate.expire(key, 1, TimeUnit.DAYS);
//        return true;
//    }
}
