package lightsoff.service;

import lightsoff.entity.Comment;
import lightsoff.entity.Score;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class CommentServiceRestClient implements CommentService{
    private final String url = "http://localhost:8080/api/comment";

    @Autowired
    private RestTemplate restTemplate;
    //private RestTemplate restTemplate = new RestTemplate();

    @Override
    public void addComment(Comment comment) {
        restTemplate.postForEntity(url, comment, Comment.class);
    }

    @Override
    public List<Comment> getComments(String gameName) {
        return Arrays.asList(restTemplate.getForEntity(url + "/" + gameName, Comment[].class).getBody());
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported via web service");
    }
}
