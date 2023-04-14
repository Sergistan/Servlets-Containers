package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
    private PostController controller;
    private final static String GET = "GET";
    private final static String POST = "POST";
    private final static String DELETE = "DELETE";
    private final static String generalPATH = "/api/posts";
    private final static String pathForID = "/api/posts/\\d+";

    @Override
    public void init() {
        final var repository = new PostRepository();
        final var service = new PostService(repository);
        controller = new PostController(service);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();

            if (method.equals(GET) && path.equals(generalPATH)) {
                controller.all(resp);
                return;
            }
            if (method.equals(GET) && path.matches(pathForID)) {
                final long id = getId(path);
                controller.getById(id, resp);
                return;
            }
            if (method.equals(POST) && path.equals(generalPATH)) {
                controller.save(req.getReader(), resp);
                return;
            }
            if (method.equals(DELETE) && path.matches(pathForID)) {
                final long id = getId(path);
                controller.removeById(id, resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private long getId(String path) {
        final var id = Long.parseLong(path.substring(path.lastIndexOf("/")).replace("/",""));
        return id;
    }
}

