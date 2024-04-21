package service;

import lightsoff.entity.Comment;
import lightsoff.service.CommentService;
import lightsoff.service.CommentServiceJDBC;
import lightsoff.service.CommentServiceJPA;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class CommentServiceTest {

    private CommentService commentService = new CommentServiceJPA();

    @Test
    public void reset() {
        commentService.reset();
        assertEquals(0, commentService.getComments("Lights off").size());
    }

    @Test
    public void addComment() {
        var date = new Date();
        var comment = new Comment("Lights off", "Valeri", "I like this game!", new Timestamp(date.getTime()));
        commentService.reset();
        commentService.addComment(comment);
        var comments = commentService.getComments("Lights off");
        assertEquals(1, comments.size());
        assertEquals("Valeri", comments.get(0).getPlayer());
        assertEquals("Lights off", comments.get(0).getGame());
        assertEquals("I like this game!",comments.get(0).getComment());
        assertEquals(new Timestamp(date.getTime()),comments.get(0).getCommentedOn());
    }

    @Test
    public void getComments(){
        var date = new Date();
        commentService.reset();
        commentService.addComment(new Comment("Lights off", "Jaroslav", "Best game for two players!", new Timestamp(date.getTime())));
        commentService.addComment(new Comment("Lights off", "Valeri", "Super! I am waiting for new version!", new Timestamp(date.getTime() + 1)));
        commentService.addComment(new Comment("Lights off", "Alla", "Great)", new Timestamp(date.getTime() + 2)));

        var comments = commentService.getComments("Lights off");
        assertEquals(3, comments.size());
        assertEquals("Alla", comments.get(0).getPlayer());
        assertEquals("Great)", comments.get(0).getComment());
        assertEquals("Valeri", comments.get(1).getPlayer());
        assertEquals("Super! I am waiting for new version!", comments.get(1).getComment());
        assertEquals("Jaroslav", comments.get(2).getPlayer());
        assertEquals("Best game for two players!", comments.get(2).getComment());
    }

}
