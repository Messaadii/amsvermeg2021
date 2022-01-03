package com.sip.ams.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sip.ams.entities.Article;
import com.sip.ams.repositories.ArticleRepository;
import com.sip.ams.repositories.ProviderRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping({"/articles"})
public class ArticleController {

    public static final String uploadDirectory = System.getProperty("user.dir") + "/src/main/resources/static/uploads";
    private final ArticleRepository articleRepository;
    private final ProviderRepository providerRepository;

    @Autowired
    public ArticleController(ArticleRepository articleRepository, ProviderRepository
            providerRepository) {
        this.articleRepository = articleRepository;
        this.providerRepository = providerRepository;
    }

    @GetMapping("/list")
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    @GetMapping("/byProvider/{providerId}")
    public List<Article> getAllArticles(@PathVariable(value = "providerId") Long providerId) {
        return articleRepository.findByProviderId(providerId);
    }

    @PostMapping("/add/{providerId}")
    Article createArticle(@PathVariable(value = "providerId") Long providerId,
                          @RequestParam("label") String label,
                          @RequestParam("price") String price,
                          @RequestParam("image") MultipartFile file) {

        Article article = new Article(label,Float.parseFloat(price));
        return providerRepository.findById(providerId).
                map(provider -> {
                    article.setPicture(saveImage(file, null));
                    article.setProvider(provider);
                    return articleRepository.save(article);
                }).orElseThrow(() -> new IllegalArgumentException("ProviderId " + providerId + " not found"));

    }

    @PutMapping("/update/{providerId}/{articleId}")
    public Article updateArticle(@PathVariable(value = "providerId") Long providerId,
                                 @PathVariable(value = "articleId") Long articleId,
                                 @RequestParam("label") String label,
                                 @RequestParam("price") String price,
                                 @RequestParam(value = "image" , required = false) MultipartFile file) {

        if (!providerRepository.existsById(providerId)) {
            throw new IllegalArgumentException("ProviderId " + providerId + " not found");
        }
        return articleRepository.findById(articleId).map(article -> {

            if(file == null)
                article.setPicture(article.getPicture());
            else
                article.setPicture(saveImage(file, articleId));
            article.setPrice(Float.parseFloat(price));
            article.setLabel(label);
            return articleRepository.save(article);
        }).orElseThrow(() -> new IllegalArgumentException("ArticleId " + articleId + "not found"));
    }

    @DeleteMapping("/delete/{articleId}")
    public ResponseEntity<?> deleteArticle(@PathVariable(value = "articleId") Long articleId) {
        return articleRepository.findById(articleId).map(article -> {
            deleteImage(articleId);
            articleRepository.delete(article);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new IllegalArgumentException("Article not found with id " + articleId));
    }

    @GetMapping("/{articleId}")
    public Article getProvider(@PathVariable Long articleId) {
        Optional<Article> p = articleRepository.findById(articleId);
        return p.get();
    }

    public String saveImage(MultipartFile file, Long articleId){

        if(articleId != null){
            deleteImage(articleId);
        }

        StringBuilder fileName = new StringBuilder();

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        String dateString = dateFormat.format(LocalDateTime.now());

        String fileDate = dateString + file.getOriginalFilename();
        fileName.append(fileDate);
        try {
            Files.write( Paths.get(uploadDirectory, fileDate) , file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName.toString();
    }

    public void deleteImage(Long articleId){
        Article article = articleRepository.findById(articleId).get();
        try{
            File f= new File(uploadDirectory+"/"+article.getPicture());
            f.delete();
        }catch (Exception e){
            System.out.println("Err deleting image");
        }
    }
}