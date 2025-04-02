package persistence;

// Importação das classes necessárias para manipulação do banco de dados SQLite no Android
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

// Classe responsável pela criação, manutenção e manipulação do banco de dados SQLite
public class TaskDatabaseHelper extends SQLiteOpenHelper {

    // Nome do banco de dados
    private static final String DATABASE_NAME = "tasks.db";

    // Versão do banco de dados (usada para controle de atualizações)
    private static final int DATABASE_VERSION = 1;

    // Nome da tabela e colunas
    private static final String TABLE_TASKS = "tasks";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TASK = "task";

    // Comando SQL para criar a tabela tasks
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_TASKS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // ID autoincrementado
                    COLUMN_TASK + " TEXT NOT NULL);"; // Coluna para armazenar a tarefa (obrigatória)

    /**
     * Construtor da classe, que chama o super construtor do SQLiteOpenHelper.
     * @param context - Contexto da aplicação (necessário para acessar recursos do Android)
     */
    public TaskDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Chamado automaticamente quando o banco de dados é criado pela primeira vez.
     * Aqui criamos a tabela usando a query definida anteriormente.
     * @param db - Instância do banco de dados
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    /**
     * Chamado quando há uma atualização na versão do banco de dados.
     * Aqui, removemos a tabela antiga (se existir) e criamos uma nova.
     * Isso pode levar à perda de dados, sendo necessário um processo de migração em casos reais.
     * @param db - Instância do banco de dados
     * @param oldVersion - Versão antiga do banco
     * @param newVersion - Nova versão do banco
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS); // Remove a tabela antiga
        onCreate(db); // Cria a nova tabela
    }

    /**
     * Método para adicionar uma nova tarefa ao banco de dados.
     * @param task - Descrição da tarefa a ser adicionada
     * @return true se a inserção for bem-sucedida, false caso contrário.
     */
    public boolean addTask(String task) {
        SQLiteDatabase db = this.getWritableDatabase(); // Obtém banco em modo escrita
        ContentValues values = new ContentValues(); // Objeto para armazenar os valores da nova linha
        values.put(COLUMN_TASK, task); // Adiciona a tarefa ao objeto de valores

        long result = db.insert(TABLE_TASKS, null, values); // Insere no banco
        db.close(); // Fecha a conexão com o banco
        return result != -1;  // Retorna true se a inserção foi bem-sucedida (o método insert retorna -1 se falhar)
    }

    /**
     * Método para remover uma tarefa do banco de dados.
     * @param task - Tarefa a ser removida
     */
    public void deleteTask(String task) {
        SQLiteDatabase db = this.getWritableDatabase(); // Obtém banco em modo escrita
        db.delete(TABLE_TASKS, COLUMN_TASK + " = ?", new String[]{task}); // Deleta a tarefa onde o nome for igual ao passado
        db.close(); // Fecha a conexão com o banco
    }

    /**
     * Método para obter todas as tarefas armazenadas no banco de dados.
     * @return Lista de tarefas armazenadas.
     */
    public ArrayList<String> getAllTasks() {
        ArrayList<String> tasks = new ArrayList<>(); // Lista para armazenar as tarefas
        SQLiteDatabase db = this.getReadableDatabase(); // Obtém banco em modo leitura
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TASKS, null); // Consulta todas as tarefas

        // Se houver resultados, percorre cada linha e adiciona à lista
        if (cursor.moveToFirst()) {
            do {
                tasks.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK))); // Obtém o valor da coluna "task"
            } while (cursor.moveToNext());
        }

        cursor.close(); // Fecha o cursor para liberar recursos
        db.close(); // Fecha a conexão com o banco
        return tasks; // Retorna a lista de tarefas
    }
}