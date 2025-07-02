package com.jongkeun.memo_app.repository;

import com.jongkeun.memo_app.model.Memo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemoRepository {
    private final JdbcTemplate jdbcTemplate;

    // 환경변수
    @Autowired
    private Environment env;

    public MemoRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Memo> memoRowMapper = (resultSet, rowNum) ->
            new Memo(
                    resultSet.getInt("id"),
                    resultSet.getString("title"),
                    resultSet.getString("content")
            );

    public List<Memo> findAll(){
        System.out.println("APP_NAME: " + env.getProperty("APP_NAME"));

        return jdbcTemplate.query(
                "select * from memo order by id desc",
                memoRowMapper
        );
    }

    public Memo findById(int id){
        return jdbcTemplate.queryForObject(
                "select * from memo where id = ?",
                memoRowMapper,
                id
        );
    }

    public void save(String title, String content){
        jdbcTemplate.update(
                "insert into memo (title, content) values (?, ?)", title, content
        );
    }

    public void update(int id, String title, String content){
        jdbcTemplate.update(
                "update memo set title = ?, content = ? where id = ?",
                title, content, id
        );
    }

    public void delete(int id){
        jdbcTemplate.update(
                "delete from memo where id = ?",
                id
        );
    }
}
