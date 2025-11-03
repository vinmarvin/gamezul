class CommandWords {
    // Array konstan yang menyimpan semua kata perintah yang valid
    private static final String validCommands[] = {
        "go", "quit", "help", "greet" //
    };

    //Periksa apakah string yang diberikan adalah kata perintah yang valid.
    public boolean isCommand(String aString) {
        for (int i = 0; i < validCommands.length; i++) {
            if (validCommands[i].equals(aString)) //
                return true;
        }
        //string tidak ditemukan di list perintah
        return false;
    }
}