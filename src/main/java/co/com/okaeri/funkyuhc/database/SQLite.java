package co.com.okaeri.funkyuhc.database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import co.com.okaeri.funkyuhc.database.Database; // import the database class.
import co.com.okaeri.funkyuhc.FunkyUHC; // import your main class


public class SQLite extends Database{
    String dbname;
    public SQLite(FunkyUHC main){
        super(main);
        dbname = plugin.getName(); // Set the table name here e.g player_kills
    }

    public String SQLiteCreateTokensTable = "CREATE TABLE IF NOT EXISTS players (" + // make sure to put your table name in here too.
            "`player` varchar(32) NOT NULL," + // This creates the different colums you will save data too. varchar(32) Is a string, int = integer
            "`kills` int(11) NOT NULL," +
            "`total` int(11) NOT NULL," +
            "PRIMARY KEY (`player`)" +  // This is creating 3 colums Player, Kills, Total. Primary key is what you are going to use as your indexer. Here we want to use player so
            ");"; // we can search by player, and get kills and total. If you some how were searching kills it would provide total and player.


    public String CreateEquips = "CREATE TABLE IF NOT EXISTS equips (" +
            "'id' INTEGER NOT NULL UNIQUE," +
            "'name' TEXT NOT NULL UNIQUE," +
            "'color' TEXT NOT NULL UNIQUE," +
            "'capitan' TEXT NOT NULL UNIQUE," +
            "'players' TEXT NOT NULL UNIQUE," +
            "PRIMARY KEY('id')" +
            ");";

    public String CreateMapSizes = "CREATE TABLE IF NOT EXISTS mapSizes(" +
            "'id' TEXT NOT NULL UNIQUE," +
            "'size' INTEGER NOT NULL," +
            "PRIMARY KEY('id'));";

    public void updateSize(int size){
        try {
            Statement s = connection.createStatement();
            String sql = "UPDATE 'main'.'mapSizes' SET 'size'="+ size + " WHERE id = 'size'";
            s.executeUpdate(sql);
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // SQL creation stuff, You can leave the blow stuff untouched.
    public Connection getSQLConnection() {
        File dataFolder = new File(plugin.getDataFolder(), dbname+".db");
        if (!dataFolder.exists()){
            try {
                //noinspection ResultOfMethodCallIgnored
                dataFolder.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "File write error: "+dbname+".db");
            }
        }
        try {
            if(connection!=null&&!connection.isClosed()){
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return connection;
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE,"SQLite exception on initialize", ex);
            // TODO: Arreglar error de que la subcarpeta FunkyoUHC no existe dentro de la carpeta plugins

        } catch (ClassNotFoundException ex) {
            plugin.getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
        }
        return null;
    }

    public void load() {
        connection = getSQLConnection();
        try {
            Statement s = connection.createStatement();
            s.executeUpdate(SQLiteCreateTokensTable);
            s.executeUpdate(CreateEquips);
            s.executeUpdate(CreateMapSizes);
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initialize();
    }
}