package com.lagoon.backend.controller;

import com.lagoon.backend.model.Comment;
import com.lagoon.backend.model.Photo;
import com.lagoon.backend.model.User;
import com.lagoon.backend.service.CommentService;
import com.lagoon.backend.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by lede on 4/29/16.
 */

@RestController
@RequestMapping("/rest")
public class PhotoResource {

    private String imageName;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private CommentService commentService;

    @RequestMapping(value = "/photo/upload", method = RequestMethod.POST)
    public String upload(HttpServletResponse response, HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Iterator<String> it = multipartRequest.getFileNames();
        MultipartFile multipartFile = multipartRequest.getFile(it.next());
        String fileName = multipartFile.getOriginalFilename();
        imageName=fileName;

        String path = new File("target/classes/static/images").getAbsolutePath()+"/"+fileName;
        try {
            multipartFile.transferTo(new File(path));
            System.out.println(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Upload Success!";
    }

    @RequestMapping(value = "/photo/add", method = RequestMethod.POST)
    public Photo addPhoto(@RequestBody Photo photo) {
        photo.setImageName(imageName);
        return photoService.save(photo);
    }

    @RequestMapping(value="/photo/user", method = RequestMethod.POST)
    public List<Photo> getPhotosByUser (@RequestBody User user) {
        return photoService.findByUser(user);
    }

    @RequestMapping(value="/photo/photoId", method = RequestMethod.POST)
    public Photo getPhotoByPhotoId (@RequestBody Long photoId) {
        return photoService.findByPhotoId(photoId);
    }

    @RequestMapping(value = "/photo/update", method = RequestMethod.POST)
    public void updatePhoto(@RequestBody Photo photo) {
        Photo currentPhoto = photoService.findByPhotoId(photo.getPhotoId());
        currentPhoto.setLikes(photo.getLikes());
        photoService.save(currentPhoto);

//        List<Comment> list = commentService.findByPhotoId(currentPhoto.getPhotoId());
//
//        for (Comment comment : list) {
//            comment.setPhoto(currentPhoto);
//            commentService.save(comment);
//        }
    }
}
