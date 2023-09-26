package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Produto {
	private int id;
	private String nome;
	private float preco;
	private int quantidade;
	private LocalDateTime dataFabricacao;	
	private LocalDate dataValidade;
	
	/*
	public static int colSize = 7;
	public static String[] attributeNames = {
	        "id",
	        "nome",
	        "preco",
	        "quantidade",
	        "dataFabricacao",
	        "dataValidade"
	};*/ //new operations do not require those two anymore
	
	///CONSTRUCTORS
	public Produto() {
		//empty class. Not recommended as it would cause many errors if inserted
	}
	public Produto(int id) { //as id is pk atleast the id is needed. As of now theres no method to check if the pk already exists inside
		this.id = id;
		nome = "";
		preco = 0.00F;
		quantidade = 0;
		dataFabricacao = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
		dataValidade = LocalDate.now().plusMonths(6); // o default é uma validade de 6 meses.
	}
	
	public Produto(int id, String descricao, float preco, int quantidade, LocalDateTime fabricacao, LocalDate v) {
		setId(id);
		setNome(descricao);
		setPreco(preco);
		setQuantidade(quantidade);
		setDataFabricacao(fabricacao);
		setDataValidade(v);
	}		
	
    public String sqlQueryAll() {
        return "(" +
               getID() + ", " +
               "'" + getNome() + "', " +
               getPreco() + ", " +
               getQuantidade() + ", " +
               "'" + getDataFabricacao() + "', " +
               "'" + getDataValidade() + "'" +
               ")";
    }
	///OTHER FUNCTIONS
	public void setDataFabricacao(LocalDateTime dataFabricacao) {
		// Pega a Data Atual
		LocalDateTime agora = LocalDateTime.now();
		// Garante que a data de fabricação não pode ser futura
		if (agora.compareTo(dataFabricacao) >= 0)
			this.dataFabricacao = dataFabricacao;
	}

	public void setDataValidade(LocalDate dataValidade) {
		// a data de fabricação deve ser anterior é data de validade.
		
		this.dataValidade = dataValidade;
	}

	public boolean emValidade() {
		return LocalDateTime.now().isBefore(this.getDataValidade().atTime(23, 59));
	}
	
	///OVERRIDES FOR STRING METHODS
	@Override
	public String toString() {
		return "Produto: " + nome + "   Preço: R$" + preco + "   Quantidade.: " + quantidade + "   Fabricação: "
				+ dataFabricacao  + "   Data de Validade: " + dataValidade;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (this.getID() == ((Produto) obj).getID());
	}	
	
	///GENERIC SETS AND GETS
	
	public int getID() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public float getPreco() {
		return preco;
	}

	public void setPreco(float preco) {
		this.preco = preco;
	}

	public int getQuantidade() {
		return quantidade;
	}
	
	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	
	public LocalDate getDataValidade() {
		return dataValidade;
	}

	public LocalDateTime getDataFabricacao() {
		return dataFabricacao;
	}

}