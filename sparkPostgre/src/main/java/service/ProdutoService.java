package service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import app.HtmlTemplateReader;
import dao.ProdutoDAO;
import model.Produto;
import spark.Request;
import spark.Response;

public class ProdutoService {
	private ProdutoDAO produtoDAO = new ProdutoDAO();
	private ArrayList<Produto> produtos; 
	//Heres the breakdown of how the function should operate:
	//1- array asigned to db current values
	//2- function receive delete or insert calls that requires updating the db
	//3- instead of getting all values from the database again with List<Produto> we can simply remove or add another Produto from the list
	
	String template;
	CustomResponse customResponse = new CustomResponse();
	//response to be sent.Not recommended to be global but it will be simpler left as is for now
	
	public ProdutoService() {
		//read html template
		try {
			template = HtmlTemplateReader.readTxtFile("insertTemplate.txt");
			System.out.println("TEMPLATE:" + "\n" + template);
		} catch (IOException e) {
			System.out.println("CRITICAL: TEMPLATE NOT FOUND");
		}
		
		produtos = new ArrayList<>(); //declare new arrayList
		produtos.addAll(produtoDAO.get()); //cals produtoDAO get to receive all Produto inside database
		
		System.out.println("PRODUTOS");
		printList(); //check values returned from DAO
		
		System.out.println("--FINISHED CONSTRUCTION--");
		makeListHtml();
	}
	

	
	public String makeListHtml() {
		//Uses the <listarray> for class Produto and makes the html list based on template
		//This function returns to post and get calls with the new html for display list
		String list = "";
		for (Produto p : produtos) {
			String replacedTemplate = template;
			
			//extremely unoptimized, both with sending a whole html out all the way to recreating a huge string 3 times
			replacedTemplate = replacedTemplate.replaceFirst("<!--ID-->", Integer.toString(p.getID()));
			replacedTemplate = replacedTemplate.replaceFirst("<!--NOME-->", p.getNome());
			replacedTemplate= replacedTemplate.replaceFirst("<!--PRECO-->", Float.toString(p.getPreco()));
			
			list += replacedTemplate;
		}
		
		//System.out.println("HTML"+"\n"+list);
		return list;
	}
	
	
	
	public Object delete(Request request, Response response) {	
		System.out.println("delete attempt");
		
		String requestBody = request.body(); //get form body
		String idStr = extractFormParam(requestBody, "id"); 
		
		
		int id = isKey(idStr);
		
		if (id != Integer.MIN_VALUE) {
			produtoDAO.deleteByKey(id);
			removeProdutoFromList(id);
		}
		
		
		//Needs to update list
	    return makeListHtml();
	}
	
	
	
	////REQUERIMENTS: ADD WARNINGS BASED ON WRONG INPUTS ON WEBSITE
	public Object insert(Request request, Response response) {
		System.out.println("insert attempt");
		
		Produto produto = extractForm(request);
		
		//Quick response given after needing to handle the json here. If this
		//is the handling method, customResponse becomes obsolete and a String array is all thats needed
		JSONObject jsonResponse = new JSONObject();
		
		
		if (produto != null) {
			jsonResponse.put("status", 1);
			produtos.add(produto); //maybe a method to print all produtos for testing could be worth it
			produtoDAO.insert(produto);
		}
		else {
			jsonResponse.put("status", 0);
			JSONArray errorsArray = new JSONArray();
			
			
			String[] s = customResponse.getAllErrors();
			for (int i = 0; i < customResponse.getErrorNum();i++) {
				System.out.println(s[i]); //debug
				errorsArray.add(s[i]);
			}
			
			jsonResponse.put("errors", errorsArray);
		}
		//Needs to update list
		//customResponse.setData(makeListHtml());
		jsonResponse.put("data", makeListHtml());
		response.type("application/json");
		return jsonResponse.toJSONString();
	}


	public Produto extractForm(Request request) {
		customResponse = new CustomResponse(); //clear previous response
		String requestBody = request.body(); //get form body
        String idStr = extractFormParam(requestBody, "id"); 
        
        int id = 0;
        try {
        	id = isKey(idStr); //check if id exists
        	if (produtoDAO.checkByKey(id)) 
        		customResponse.addError("ID ja existe");
        }catch(NumberFormatException e) {
        	customResponse.addError("ID Invalida");
        }
	
		//extract other form values (Str for values that will be converted later)
        String nome = extractFormParam(requestBody, "nome");
        
        //can give exceptions
        String precoStr = extractFormParam(requestBody, "preco");
        String quantidadeStr = extractFormParam(requestBody, "quantidade");
        
        float preco = 0;
	    //conversions
        try {
        	preco = Float.parseFloat(precoStr);
        } catch(NumberFormatException e) {
        	customResponse.addError("preco invalido");
        }
        
        int quantidade = 0;
        try {
        	quantidade = Integer.parseInt(quantidadeStr);
        } catch(NumberFormatException e) {
        	customResponse.addError("quantidade invalida");
        }
		
			
		//seems just parsing wont work. Ill ignore them for now
		//LocalDateTime dataFabricacao = LocalDateTime.parse(dataFabricacaoStr);
		//LocalDate dataValidade = LocalDate.parse(dataValidadeStr);
		// ->
		LocalDateTime dataFabricacao = LocalDateTime.of(2004, 7, 2, 12, 0, 0);
		LocalDate dataValidade = LocalDate.of(2004, 7, 2);
		
		Produto produto;
		if(customResponse.getErrorNum() > 0) produto = null;
		else produto = new Produto(id,nome,preco,quantidade,dataFabricacao,dataValidade);
		
		//System.out.println("insert produto :" + produto.toString());
		return produto;
	}
	///auxiliary functions
	
	public int isKey(String idStr) throws NumberFormatException{
		int i = Integer.MIN_VALUE;
		i = Integer.parseInt(idStr);
		
		return i;
	}
	/*
	public int isKey(String idStr) {
		int i = Integer.MIN_VALUE;
		try {
			i = Integer.parseInt(idStr);
		}
		catch (NumberFormatException e) {
			System.out.println("conversion to integer id" + e.getMessage() + " from isKey()");
		}
		return i;
	}
	*/
	
	
	public Produto removeProdutoFromList(int idToRemove) {
		Produto produtoToRemove = null;
		
		//look for id
		for (Produto produto : produtos) {
		    if (produto.getID() == idToRemove) {
		        produtoToRemove = produto;
		        break;
		    }
		}

		// If a matching Produto was found, remove it from the list
		if (produtoToRemove != null) {
		    produtos.remove(produtoToRemove);
		} else {
		    System.out.println("Produto with id " + idToRemove + " not found.");
		}
		return produtoToRemove;
	}
	

	public void printList() {
		for (Produto produto : produtos) {
		    System.out.println(produto.toString());
		}
	}
	///this method may not work for other types of form requests. Detailed study of how they are organized later on will be needed
	private static String extractFormParam(String body, String paramName) {
        // Search for the paramName in the request body
        String paramStart = "name=\"" + paramName + "\"\r\n\r\n";
        
        int startIdx = body.indexOf(paramStart); //return index of first char (n) in string name=paramName\r\sn\r\n
        if (startIdx == -1) {
            return null; // indexOf returns -1 if not found
        }
         
        // Find the end boundary for this parameter
        String boundary = body.substring(0, body.indexOf("\r\n")); //subtring returns String of index X to index Y. Doesnt include index Y
        //in this case it will return "----WebKitFormBoundaryAXDWEfIMLRujJDW9"
        	
        
        int endIdx = body.indexOf(boundary, startIdx); //starts from startIdx -> (n) in string name=paramName\r\n\r\n
        											   //looks for first ocurrence of boundary => "----WebKitFormBoundaryAXDWEfIMLRujJDW9"
        											   //assuming boundary comes next you should be abled to get the index of first '-'
        
        if (endIdx == -1) { //since for this form "----WebKitFormBoundaryAXDWEfIMLRujJDW9" appears at the end for all values, this will work
            return null; 
        }

        // Extract the parameter value
        return body.substring(startIdx + paramStart.length(), endIdx - 2); // -1 goes to \n and -2 goes to \r. 
        																   //substring apparently doesnt include last char
    }
}
