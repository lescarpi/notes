package lescarpi.me.notes.note;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.ObjectMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class NoteRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void save(Note note) {
        String sql = "INSERT INTO notes (id, content, created_at) VALUES (?, ?::jsonb, ?)";
        jdbcTemplate.update(sql, note.id(), note.content().toString(), note.createdAt());
    }

    public Optional<Note> findById(UUID id) {
        String sql = "SELECT id, content, created_at FROM notes WHERE id = ?";
        List<Note> results = jdbcTemplate.query(sql, this::mapRow, id);
        return results.stream().findFirst();
    }

    public List<Note> findAll(int limit, int offset) {
        String sql = "SELECT id, content, created_at FROM notes ORDER BY created_at DESC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, this::mapRow, limit, offset);
    }

    public void update(Note note) {
        String sql = "UPDATE notes SET content = ?::jsonb WHERE id = ?";
        jdbcTemplate.update(sql, note.content().toString(), note.id());
    }

    public void deleteById(UUID id) {
        String sql = "DELETE FROM notes WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean existsById(UUID id) {
        String sql = "SELECT COUNT(1) FROM notes WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    private Note mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            return new Note(
                    rs.getObject("id", UUID.class),
                    objectMapper.readTree(rs.getString("content")),
                    rs.getObject("created_at", OffsetDateTime.class)
            );
        } catch (Exception e) {
            throw new SQLException("Erro ao fazer parse do conteúdo JSONB da nota", e);
        }
    }

}
