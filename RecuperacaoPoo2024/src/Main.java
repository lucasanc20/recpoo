import java.sql.*;

public class Main {
    static final String DB_URL = "jdbc:sqlite:clothing_store.db";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DriverManager.getConnection(DB_URL);

            // Criar tabela "roupas" se não existir
            criarTabelaRoupas(conn);

            // Inserir um novo registro na tabela
            System.out.println("Inserindo registro na tabela...");
            inserirRoupa(conn, new Roupa("Camiseta", "M", "Azul", 25.00));
            System.out.println("Registro inserido com sucesso!");

            // Atualizar um registro na tabela
            System.out.println("Atualizando registro na tabela...");
            atualizarPrecoRoupa(conn, "Camiseta", 20.00);
            System.out.println("Registro atualizado com sucesso!");

            // Excluir um registro na tabela
            System.out.println("Excluindo registro da tabela...");
            excluirRoupa(conn, "Camiseta");
            System.out.println("Registro excluído com sucesso!");

            // Consultar os registros na tabela
            System.out.println("Consultando registros na tabela...");
            consultarRoupas(conn);

        } catch (SQLException se) {
            // Tratamento de erros do JDBC
            se.printStackTrace();
        } finally {
            // Fechando os recursos em um bloco finally
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
                // Nada a fazer
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        System.out.println("Fim do programa");
    }

    // Método para criar a tabela "roupas" se não existir
    private static void criarTabelaRoupas(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS roupas (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "tipo TEXT," +
                    "tamanho TEXT," +
                    "cor TEXT," +
                    "preco REAL)";
            stmt.executeUpdate(sql);
        }
    }

    // Método para inserir uma nova roupa na tabela
    private static void inserirRoupa(Connection conn, Roupa roupa) throws SQLException {
        String sql = "INSERT INTO roupas (tipo, tamanho, cor, preco) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, roupa.getTipo());
            stmt.setString(2, roupa.getTamanho());
            stmt.setString(3, roupa.getCor());
            stmt.setDouble(4, roupa.getPreco());
            stmt.executeUpdate();
        }
    }

    // Método para atualizar o preço de uma roupa na tabela
    private static void atualizarPrecoRoupa(Connection conn, String tipo, double novoPreco) throws SQLException {
        String sql = "UPDATE roupas SET preco=? WHERE tipo=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, novoPreco);
            stmt.setString(2, tipo);
            stmt.executeUpdate();
        }
    }

    // Método para excluir uma roupa da tabela
    private static void excluirRoupa(Connection conn, String tipo) throws SQLException {
        String sql = "DELETE FROM roupas WHERE tipo=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tipo);
            stmt.executeUpdate();
        }
    }

    // Método para consultar as roupas na tabela e imprimir os registros
    private static void consultarRoupas(Connection conn) throws SQLException {
        String sql = "SELECT * FROM roupas";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String tipo = rs.getString("tipo");
                String tamanho = rs.getString("tamanho");
                String cor = rs.getString("cor");
                double preco = rs.getDouble("preco");

                System.out.println("ID: " + id);
                System.out.println("Tipo: " + tipo);
                System.out.println("Tamanho: " + tamanho);
                System.out.println("Cor: " + cor);
                System.out.println("Preço: " + preco);
                System.out.println();
            }
        }
    }
}

// Classe Roupa
class Roupa {
    private String tipo;
    private String tamanho;
    private String cor;
    private double preco;

    public Roupa(String tipo, String tamanho, String cor, double preco) {
        this.tipo = tipo;
        this.tamanho = tamanho;
        this.cor = cor;
        this.preco = preco;
    }

    public String getTipo() {
        return tipo;
    }

    public String getTamanho() {
        return tamanho;
    }

    public String getCor() {
        return cor;
    }

    public double getPreco() {
        return preco;
    }
}
