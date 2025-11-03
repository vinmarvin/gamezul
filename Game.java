import java.util.Random;

/**
 * Ini adalah kelas utama dari aplikasi "World of Zuul". 
 * "World of Zuul" adalah permainan petualangan berbasis teks yang sangat sederhana.
 * Pengguna dapat berjalan di sekitar beberapa ruangan.
 * * Versi ini telah dimodifikasi untuk:
 * 1. Menggunakan Bahasa Indonesia.
 * 2. Menambahkan mekanik "Penjaga" acak.
 * 3. Menambahkan kondisi menang (mencapai 'lab').
 * 4. Menambahkan kondisi kalah (ditangkap penjaga).
 */

class Game {
    private Parser parser;
    private Room currentRoom;
    
    // Variabel untuk mekanik kustom
    private Room startRoom;  // Ruang tempat pemain mulai
    private Room goalRoom;   // Ruang tujuan untuk menang
    private Random random;  
    private boolean guardIsPresent; // Status apakah penjaga
    private boolean guardIsGreeted; // Status apakah penjaga sudah disapa

    /**
     * Buat game dan inisialisasi petanya.
     */
    public Game() {
        createRooms();
        parser = new Parser(); 
        // Inisialisasi untuk mekanik penjaga
        random = new Random();
        guardIsPresent = false; 
        guardIsGreeted = false;
    }

    private void createRooms() {
        Room outside, kantin, aula, sidang, kolaborasi, k101, k102, k103, k104, k105, lab;

        // buat ruangan
        outside = new Room("di luar gerbang utama universitas"); 
        kantin = new Room("di Kantin");
        aula = new Room("di Aula");
        sidang = new Room("di Ruang Sidang");
        kolaborasi = new Room("di Ruang Kolaborasi");
        k101 = new Room("di Ruang Kelas 101");
        k102 = new Room("di Ruang Kelas 102");
        k103 = new Room("di Ruang Kelas 103");
        k104 = new Room("di Ruang Kelas 104");
        k105 = new Room("di Ruang Kelas 105");
        lab = new Room("di Ruangan Laboratorium");

        // atur pintu keluar (North, East, South, West)
        outside.setExits(aula, kantin, null, null); 
        kantin.setExits(null, null, null, outside); 
        aula.setExits(kolaborasi, null, outside, sidang); 
        sidang.setExits(null, aula, null, null); 
        kolaborasi.setExits(k101, k102, aula, k103);
        k101.setExits(k104, null, kolaborasi, null);
        k102.setExits(null, null, null, kolaborasi);
        k103.setExits(null, kolaborasi, null, null);
        k104.setExits(k105, null, k101, null);
        k105.setExits(lab, null, k104, null);
        lab.setExits(null, null, k105, null);

        currentRoom = outside;
        startRoom = outside; 
        goalRoom = lab;      // Tentukan 'lab' (Ruangan Laboratorium) sebagai tujuan
    }

    // Loop utama permainan.
    public void play() {
        printWelcome(); //

        boolean finished = false;
        while (!finished) {
            Command command = parser.getCommand(); 
            finished = processCommand(command); 
        }
        System.out.println("Terima kasih telah bermain. Sampai jumpa."); 
    }
    
    private void printExits(Room room) {
        System.out.print("Pintu keluar: "); 
        if (room.getNorthExit() != null) System.out.print("north "); 
        if (room.getEastExit()  != null) System.out.print("east "); 
        if (room.getSouthExit() != null) System.out.print("south "); 
        if (room.getWestExit()  != null) System.out.print("west "); 
        System.out.println(); 
    }

    private void printWelcome() {
        System.out.println(); 
        System.out.println("Selamat Datang di Petualangan!");
        System.out.println("Hari sudah sore, capai laboratorium komputer untuk melanjutkan tugas anda, jangan bertindak mencurigakan atau anda akan dikeluarkan.");
        System.out.println("Ketik 'help' jika Anda butuh bantuan."); 
        System.out.println(); 
        System.out.println("Anda berada " + currentRoom.getDescription());
        printExits(currentRoom); 
    }


    private boolean processCommand(Command command) {
        boolean wantToQuit = false;

        if (command.isUnknown()) {
            System.out.println("Saya tidak tahu apa maksud Anda...");
            return false;
        }

        String commandWord = command.getCommandWord(); 

        //Jika ditangkap, panggil capturePlayer() dan return true (untuk quit)
        if (guardIsPresent && !guardIsGreeted && !commandWord.equals("greet")) { 
            capturePlayer();
            return true; 
        }

        if (commandWord.equals("help"))
            printHelp();
        else if (commandWord.equals("go")) { //
            // LOGIKA MENANG: Jika goRoom() mengembalikan true (karena menang),
            if (goRoom(command)) { 
                wantToQuit = true;
            }
        }
        else if (commandWord.equals("quit")) 
            wantToQuit = quit(command);
        else if (commandWord.equals("greet")) 
            greetGuard(command);

        return wantToQuit;
    }

    //Cetak pesan bantuan.
    private void printHelp() {
        System.out.println("Anda tersesat. Anda sendirian. Anda berkelana");
        System.out.println("di sekitar universitas."); 
        System.out.println("Tujuanmu: Capai laboratorium komputer (" + goalRoom.getDescription() + ")");
        System.out.println(); 
        System.out.println("Perintah yang bisa Anda gunakan adalah:"); 
        System.out.println("    go   quit    help    greet");
    }

    private boolean goRoom(Command command) {
        if (!command.hasSecondWord()) { 
            System.out.println("Pergi ke mana?");
            return false; // Belum menang
        }

        String direction = command.getSecondWord();

        Room nextRoom = null;
        if (direction.equals("north")) nextRoom = currentRoom.getNorthExit(); 
        if (direction.equals("east"))  nextRoom = currentRoom.getEastExit(); 
        if (direction.equals("south")) nextRoom = currentRoom.getSouthExit(); 
        if (direction.equals("west"))  nextRoom = currentRoom.getWestExit(); 

        if (nextRoom == null) { 
            System.out.println("Tidak ada pintu ke arah sana!");
            return false; // Belum menang
        }
        else {
            currentRoom = nextRoom;
            
            // CEK KEMENANGAN
            if (currentRoom == goalRoom) {
                printWinMessage();
                return true; // MENANG! akan mengakhiri game
            }
            
            // Jika belum menang, lanjutkan seperti biasa
            System.out.println("Anda berada " + currentRoom.getDescription()); //
            printExits(currentRoom);            
            checkForGuard();
            return false; // Belum menang
        }
    }

    private boolean quit(Command command) {
        if (command.hasSecondWord()) { //
            System.out.println("Keluar dari apa?"); //
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * Mengecek secara acak apakah penjaga muncul di ruangan saat ini.
     * Ini dipanggil setiap kali pemain memasuki ruangan baru.
     */
    private void checkForGuard() {
        // Peluang 25% di ruangan baru
        if (currentRoom != startRoom && random.nextInt(4) == 0) { //
            guardIsPresent = true; 
            guardIsGreeted = false; 
            System.out.println("--------------------------------------------------"); //
            System.out.println("!PERHATIAN! Seorang penjaga sedang berdiri di ruangan ini."); //
            System.out.println("Anda harus menyapanya (ketik 'greet') sebelum melakukan hal lain."); //
            System.out.println("--------------------------------------------------"); //
        } else {
            guardIsPresent = false; 
            guardIsGreeted = false; 
        }
    }

    private void capturePlayer() {
        System.out.println("Anda mencoba pergi tanpa menyapa! Penjaga menangkap Anda."); //
        System.out.println("Anda dikeluarkan dari universitas. GAME OVER.");
    }

    private void greetGuard(Command command) {
        if (guardIsPresent && !guardIsGreeted) { //
            System.out.println("Anda menyapa penjaga. Dia mengangguk dan membiarkan Anda lewat."); //
            guardIsGreeted = true;
            guardIsPresent = false;
        } 
        else {
            System.out.println("Jangan buat tindakan aneh.");
        }
    }
    

    private void printWinMessage() {
        System.out.println("\n**************************************************");
        System.out.println("Akhirnya! Anda sampai di tujuan, selamat belajar");
        System.out.println("Anda memenangkan permainan!");
        System.out.println("**************************************************\n");
    }
    
    public static void main(String[] args) {
        Game game = new Game(); //Game baru untuk dimainkan
        game.play();
    }
}