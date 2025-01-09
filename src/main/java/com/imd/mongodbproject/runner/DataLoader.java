package com.imd.mongodbproject.runner;

import com.imd.mongodbproject.service.DocumentService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private DocumentService documentService;

    @Override
    public void run(String... args) throws Exception {
        // Gerando dados de exemplo para a coleção "restaurants"
        generateSampleDataResteurant("restaurants");
        generateSampleDataMovies("movies");
        generateSampleDataProducts("products");
    }

    private void generateSampleDataProducts(String products) {
        Document produto1 = new Document("name", "Produto A")
                .append("category", "Eletrônicos")
                .append("price", 299.99)
                .append("stock", 50);

        Document produto2 = new Document("name", "Produto B")
                .append("category", "Vestuário")
                .append("price", 79.99)
                .append("stock", 150);

        // Salvando os documentos na coleção "produtos"
        documentService.create(products, produto1);
        documentService.create(products, produto2);

    }

    private void generateSampleDataMovies(String movies) {
        // Criando documentos de exemplo para a coleção "movies"
        Document movie1 = new Document("title", "Inception")
                .append("director", "Christopher Nolan")
                .append("releaseYear", 2010)
                .append("genre", "Sci-Fi")
                .append("cast", new Document("actor1", "Leonardo DiCaprio")
                        .append("actor2", "Joseph Gordon-Levitt")
                        .append("actor3", "Ellen Page"));

        Document movie2 = new Document("title", "The Dark Knight")
                .append("director", "Christopher Nolan")
                .append("releaseYear", 2008)
                .append("genre", "Action")
                .append("cast", new Document("actor1", "Christian Bale")
                        .append("actor2", "Heath Ledger")
                        .append("actor3", "Aaron Eckhart"));

        documentService.create(movies, movie1);
        documentService.create(movies, movie2);
    }

    private void generateSampleDataResteurant(String collectionName) {
        // Criando documentos de exemplo
        Document restaurant1 = new Document("name", "Restaurant A")
                .append("borough", "Manhattan")
                .append("cuisine", "Italian")
                .append("address", new Document("building", "100")
                        .append("street", "1st Avenue")
                        .append("zipcode", "10001"));

        // Salvando os documentos na coleção "restaurants"
        documentService.create(collectionName, restaurant1);
        System.out.println("Dados de exemplo inseridos na coleção " + collectionName);
    }
}
