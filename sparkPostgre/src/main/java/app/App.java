package app;
import java.io.IOException;

import service.ProdutoService;
import spark.Request;
import spark.Response;
import spark.Spark;

import static spark.Spark.*;

public class App {
	
	public static void main (String[]args) throws IOException {
		
        staticFiles.location("/public");
        port(6788);
  
        ProdutoService produtoService = new ProdutoService();
        
	        
	        Spark.get("/", (req, res) -> {
	        	 System.out.println("Received POST request to /");

	            return App.class.getResourceAsStream("index.html");
	        });
        
	        //get("/produto/:id", (request, response) -> produtoService.get(request, response));
	        
	        //get("/produto/list/:orderby", (request, response) -> produtoService.getAll(request, response));

	        //get("/produto/update/:id", (request, response) -> produtoService.getToUpdate(request, response));
	        
	        //post("/produto/update/:id", (request, response) -> produtoService.update(request, response));
	           
	        get("/produto/delete/:id", (request, response) -> produtoService.delete(request, response));
	        
	        post("/product/insert", (request, response) -> produtoService.insert(request, response));
        
	}


}

/*
 

 */
/*
ProdutoDAO produtoDAO = new ProdutoDAO();
Produto produto1 = new Produto(1, "Produto 1", 10.99f, 100, 
		 LocalDateTime.of(2023, 9, 14, 10, 0), LocalDate.of(2023, 12, 31));
 
 //Prompt for new produtos if needed
 Produto produto2 = new Produto(2, "Produto 2", 19.99f, 50, LocalDateTime.of(2023, 9, 15, 14, 30), LocalDate.of(2023, 11, 30));
 Produto produto3 = new Produto(3, "Produto 3", 5.49f, 200, LocalDateTime.of(2023, 9, 16, 9, 15), LocalDate.of(2024, 2, 28));
 Produto produto4 = new Produto(4, "Produto 4", 7.79f, 75, LocalDateTime.of(2023, 9, 17, 16, 45), LocalDate.of(2023, 10, 31));
 Produto produto5 = new Produto(5, "Produto 5", 12.99f, 120, LocalDateTime.of(2023, 9, 18, 12, 0), LocalDate.of(2023, 11, 15));

 System.out.println(produto1.sqlQueryAll());
 produtoDAO.insert(produto1);
 Produto produtoA = produtoDAO.getByKey("1");
 System.out.println(produtoA.sqlQueryAll());
 
 port(5678);
 


 get("/oi", (request, response) -> "Oi Mundo!");
 */ //Testes de exec