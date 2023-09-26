package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Produto;

///This .java would only need the variables and class names changed, 
///as using a class that contains all columns automates most things here
public class ProdutoDAO extends DAO {
	public String dbName = " public.produtos "; //database name > between spaces for sql query
	private String keyCol = " id "; //primary key name > between spaces

	public ProdutoDAO(){
		super();
		conectar("db1","ti2cc","senha123"); //default test conection for now
	}
	
	public boolean insert(Produto tablename) { //Although the name is vague the class Tablename 
												 //should be equal to a table line (contains all col values)
		System.out.println("insert db attempt");
		boolean status = false;
		try {  
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			// example string - INSERT INTO public.tablename VALUES (...);
			String sql = "INSERT INTO" + dbName + 
					"VALUES" + tablename.sqlQueryAll();
			System.out.println("insert sql done: " + sql);
			st.executeUpdate(sql);
			st.close();
			status = true;
		} catch (SQLException e) {  
			    System.err.println("SQL Exception: " + e.getMessage());
			    e.printStackTrace(); // Print the full stack trace for debugging
		}
		return status;
	}
	
	public List<Produto> get(){
		return get("","");
	}
	
	public List<Produto> get(String col, String value) {
		List<Produto> table = new ArrayList<>();
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			// example string - SELECT * FROM public.tablename WHERE id = 1
			String sql;
			if (col.isEmpty()) sql = "SELECT * FROM" + dbName;
			else sql = "SELECT * FROM" + dbName + "WHERE " + col + "=" + "'"+value+"'";
			
			System.out.println(sql); //debug
			
			ResultSet rs = st.executeQuery(sql);	
	        while(rs.next()){
	        	table.add(new Produto(
	        		    rs.getInt("id"),
	        		    rs.getString("nome"),
	        		    rs.getFloat("preco"),
	        		    rs.getInt("quantidade"),
	        		    rs.getTimestamp("dataFabricacao").toLocalDateTime(),
	        		    rs.getDate("dataValidade").toLocalDate()
	        		));

	        }
	        st.close();
	        
	        System.out.println("RESULT FROM QUERY");
	        for (Produto produto : table) {
	        	System.out.println(produto.getID());
	        }
	        System.out.println();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return table;
	}
	
	public Produto getByKey(int value) {
		Produto db = null;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			// example string - SELECT * FROM public.tablename WHERE id = 1
			String sql = "SELECT * FROM" + dbName + "WHERE " + keyCol + "=" + value;
			System.out.println(sql);
			ResultSet rs = st.executeQuery(sql);	
	        if(rs.next()){
	        	db = new Produto(
	        		    rs.getInt("id"),
	        		    rs.getString("nome"),
	        		    rs.getFloat("preco"),
	        		    rs.getInt("quantidade"),
	        		    rs.getTimestamp("dataFabricacao").toLocalDateTime(),
	        		    rs.getDate("dataValidade").toLocalDate()
	        		);
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return db;
	}
	
	public boolean checkByKey(int value) {
		boolean exists = false;
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			// example string - SELECT * FROM public.tablename WHERE id = 1
			String sql = "SELECT * FROM" + dbName + "WHERE" + keyCol + "=" + value;
			//System.out.println(sql);
			ResultSet rs = st.executeQuery(sql);	
	        if(rs.next()) exists = true;
	        st.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return exists;
	}
	
	public boolean deleteByKey(int value) {
		boolean status = false;
		try {  
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			// example string - DELETE FROM public.tablename WHERE id = 1
			String sql = "DELETE FROM" + dbName + "WHERE " + keyCol + " = " + value;
			//System.out.println(sql);
			st.executeUpdate(sql);
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
	
}
