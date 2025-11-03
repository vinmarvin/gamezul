/**
 * Sebuah "Room" mewakili satu lokasi di peta permainan. Ia terhubung
 * ke ruangan lain melalui pintu keluar. Pintu keluar diberi label north,
 * east, south, west. Untuk setiap arah, ruangan menyimpan
 * referensi ke ruangan tetangga, atau null jika tidak ada pintu keluar ke arah itu.
 */
class Room {
    private String description; 
    private Room northExit; 
    private Room southExit; 
    private Room eastExit; 
    private Room westExit; 

    public Room(String description) {
        this.description = description;
    }

    public void setExits(Room north, Room east, Room south, Room west) {
        if (north != null) northExit = north; 
        if (east  != null) eastExit  = east; 
        if (south != null) southExit = south; 
        if (west  != null) westExit  = west; 
    }


    public String getDescription() {
        return description; 
    }
    

    public Room getNorthExit() { 
        return northExit; 
    }
    

    public Room getSouthExit() { 
        return southExit; 
    }
    

    public Room getEastExit() { 
        return eastExit; 
    }
    
    public Room getWestExit() { 
        return westExit; 
    }
}