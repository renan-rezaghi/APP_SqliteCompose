package com.example.sqlitecompose

// Importações necessárias para banco de dados, Compose e componentes de UI
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    // --- Modelo de dados da Nota ---
    data class Note(
        val id: Long? = null, // ID gerado automaticamente pelo SQLite
        val title: String,
        val content: String,
        val author: String // Novo campo adicionado: autor da nota
    )

    // --- Classe para gerenciar o banco de dados SQLite ---
    class DBHelper(context: Context) :
        SQLiteOpenHelper(context, "app.db", null, 2) { // versão 2 por causa do novo campo

        // Criação da tabela SQLite
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE notes(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT NOT NULL,
                    content TEXT NOT NULL,
                    author TEXT NOT NULL
                )
                """.trimIndent()
            )
        }

        // Atualização do banco (neste caso, recria tudo)
        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS notes")
            onCreate(db)
        }

        // Inserir nova nota no banco
        fun insertNote(note: Note): Long {
            val cv = ContentValues().apply {
                put("title", note.title)
                put("content", note.content)
                put("author", note.author)
            }
            return writableDatabase.insert("notes", null, cv)
        }

        // Atualizar uma nota existente
        fun updateNote(note: Note): Int {
            requireNotNull(note.id) { "ID não pode ser nulo para update" }
            val cv = ContentValues().apply {
                put("title", note.title)
                put("content", note.content)
                put("author", note.author)
            }
            return writableDatabase.update(
                "notes",
                cv,
                "id=?",
                arrayOf(note.id.toString())
            )
        }

        // Excluir uma nota pelo ID
        fun deleteNote(id: Long): Int {
            return writableDatabase.delete(
                "notes",
                "id=?",
                arrayOf(id.toString())
            )
        }

        // Buscar todas as notas do banco
        fun getAllNotes(): List<Note> {
            val list = mutableListOf<Note>()
            val c: Cursor = readableDatabase.rawQuery(
                "SELECT id, title, content, author FROM notes ORDER BY id DESC",
                null
            )
            c.use { cur ->
                val idIdx = cur.getColumnIndexOrThrow("id")
                val titleIdx = cur.getColumnIndexOrThrow("title")
                val contentIdx = cur.getColumnIndexOrThrow("content")
                val authorIdx = cur.getColumnIndexOrThrow("author")
                while (cur.moveToNext()) {
                    list.add(
                        Note(
                            id = cur.getLong(idIdx),
                            title = cur.getString(titleIdx),
                            content = cur.getString(contentIdx),
                            author = cur.getString(authorIdx)
                        )
                    )
                }
            }
            return list
        }
    }

    // Função principal da Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = DBHelper(this)

        setContent {
            // Define o tema e a tela principal
            MaterialTheme {
                Surface(Modifier.fillMaxSize()) {
                    NotesScreen(dbHelper = db)
                }
            }
        }
    }

    // Composable com a interface de cadastro, listagem e edição
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun NotesScreen(dbHelper: DBHelper) {
        var notes by remember { mutableStateOf(dbHelper.getAllNotes()) } // Lista de notas

        // Estados dos campos de entrada
        var title by remember { mutableStateOf(TextFieldValue("")) }
        var content by remember { mutableStateOf(TextFieldValue("")) }
        var author by remember { mutableStateOf(TextFieldValue("")) }

        var editingId by remember { mutableStateOf<Long?>(null) } // ID da nota sendo editada (se houver)

        // Função para limpar os campos do formulário
        fun clearFields() {
            title = TextFieldValue("")
            content = TextFieldValue("")
            author = TextFieldValue("")
            editingId = null
        }

        // Estrutura da tela com barra superior e formulário
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            if (editingId == null)
                                "Notas (SQLite + Compose) Renan Rezaghi"
                            else
                                "Editando #$editingId"
                        )
                    }
                )
            }
        ) { padding ->
            Column(
                Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                // Campo título
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                // Campo conteúdo
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Conteúdo") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                // Campo autor
                OutlinedTextField(
                    value = author,
                    onValueChange = { author = it },
                    label = { Text("Autor") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                // Botões de ação: salvar/atualizar e limpar
                Row {
                    Button(
                        onClick = {
                            val t = title.text.trim()
                            val c = content.text.trim()
                            val a = author.text.trim()

                            // Validação simples
                            if (t.isEmpty() || c.isEmpty() || a.isEmpty()) return@Button

                            if (editingId == null) {
                                // Inserção
                                dbHelper.insertNote(Note(title = t, content = c, author = a))
                            } else {
                                // Atualização
                                dbHelper.updateNote(
                                    Note(id = editingId, title = t, content = c, author = a)
                                )
                            }

                            // Atualiza lista e limpa campos
                            notes = dbHelper.getAllNotes()
                            clearFields()
                        }
                    ) {
                        Text(if (editingId == null) "Salvar" else "Atualizar")
                    }

                    Spacer(Modifier.width(8.dp))

                    OutlinedButton(onClick = { clearFields() }) {
                        Text("Limpar")
                    }
                }

                Spacer(Modifier.height(16.dp))
                HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
                Spacer(Modifier.height(8.dp))

                // Lista de notas
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(notes, key = { it.id ?: -1 }) { note ->
                        NoteItem(
                            note = note,
                            onClick = {
                                // Preenche os campos para edição
                                editingId = note.id
                                title = TextFieldValue(note.title)
                                content = TextFieldValue(note.content)
                                author = TextFieldValue(note.author)
                            },
                            onDelete = { id ->
                                // Deleta e atualiza lista
                                dbHelper.deleteNote(id)
                                notes = dbHelper.getAllNotes()
                                if (editingId == id) clearFields()
                            }
                        )
                        Divider()
                    }
                }
            }
        }
    }

    // Item individual da lista de notas
    @Composable
    private fun NoteItem(
        note: Note,
        onClick: () -> Unit,
        onDelete: (Long) -> Unit
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clickable { onClick() } // Permite clicar para editar
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = note.title, style = MaterialTheme.typography.titleMedium)

                // Botão excluir (só aparece se tiver ID)
                if (note.id != null) {
                    TextButton(onClick = { onDelete(note.id) }) {
                        Text("Excluir")
                    }
                }
            }

            Spacer(Modifier.height(4.dp))

            Text(text = note.content, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(2.dp))
            Text(text = "Autor: ${note.author}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
