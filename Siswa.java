import java.sql.*;
import java.util.*;

/**
 * Write a description of class Siswa here.
 *
 * @author Rohman
 * @version v0.1.0
 */
public class Siswa {
    ConnectDB db;

    public static void main(String[] args) throws Exception {
        Siswa siswa = new Siswa();
        siswa.Start();
    }

    public Siswa() throws Exception {
        this.db = new ConnectDB();
    }

    public void Start() throws Exception {
        System.out.println("Selamat Datang Di Database Sekolah");
        System.out.println("---------------------------------");

        this.selectMenu();
    }

    protected void selectMenu() {
        System.out.println("Daftar Menu");
        System.out.println("1. Tampilkan Data Siswa");
        System.out.println("2. Input Data Siswa");
        System.out.println("3. Ubah Data Siswa");
        System.out.println("4. Hapus Data Siswa");
        System.out.println("5. Cari Data Siswa");
        System.out.println();
        System.out.print("Silahkan Pilih Menu (Isi Dengan Angka): ");
        System.out.println();

        Scanner scan = new Scanner(System.in);

        Integer menu = scan.nextInt();

        scan.close();

        switch (menu) {
            case 1:
                this.showData();
                break;

            case 2:
                this.insertData();
                break;

            case 3:
                this.updateData();
                break;

            case 4:
                this.deleteData();
                break;

            case 5:
                this.searchData();
                break;

            default:
                System.out.println("Maaf, kami tidak mengenali menu " + menu + ". Silahkan coba lagi.");
                break;
        }

        this.selectMenu();
    }

    private void searchData() {
        Scanner scan = new Scanner(System.in);

        System.out.println("Masukan data yang akan di cari: ");
        String search = scan.next();

        scan.close();

        String likeQuery = " LIKE '%" + search + "%' ";
        String query = "SELECT * FROM siswa WHERE id_siswa" + likeQuery + "OR nama " + likeQuery + "OR ttl" + likeQuery
                + "OR jk" + likeQuery;
        this.getData(query);
    }

    private void showData() {
        this.getAllData(null);
    }

    private void getAllData(SiswaModel siswa) {
        String query = "SELECT * FROM siswa";

        if (siswa != null) {
            query += " WHERE ";

            if (siswa.id_siswa != null) {
                query += "id_siswa=" + siswa.id_siswa;
            }
        }

        this.getData(query);
    }

    private void getData(String query) {
        try {
            ResultSet rs = this.db.stmt.executeQuery(query);

            String format = "%-10s %-25s %-30s %-15s %n";
            System.out.printf(format, "ID Siswa", "Nama Siswa", "Tempat, Tgl lahir", "Jenis Kelamin");
            while (rs.next()) {
                System.out.printf(format, rs.getString("id_siswa"), rs.getString("nama"), rs.getString("ttl"),
                        rs.getString("jk"));
                System.out.println();
            }
            rs.close();

            this.selectMenu();
        } catch (SQLException e) {
            System.out.println("Tidak bisa mendapatkan data siswa. Error: " + e.getMessage());
        }
    }

    private void insertData() {
        try {
            Scanner scan = new Scanner(System.in);

            System.out.println("Masukan ID Siswa: ");
            int id = Integer.parseInt(scan.nextLine().trim());

            System.out.println("Masukan Nama Siswa: ");
            String nama = scan.nextLine();

            System.out.println("Masukan TTL Siswa: ");
            String ttl = scan.nextLine();

            System.out.println("Masukan Jenis Kelamin Siswa: ");
            String jk = scan.nextLine();

            scan.close();

            String query = "INSERT INTO siswa(id_siswa, nama, ttl, jk) VALUES(?,?,?,?)";
            PreparedStatement prep = this.db.conn.prepareStatement(query);

            prep.setInt(1, id);
            prep.setString(2, nama);
            prep.setString(3, ttl);
            prep.setString(4, jk);
            prep.addBatch();

            this.db.conn.setAutoCommit(false);
            prep.executeBatch();
            this.db.conn.setAutoCommit(true);

            this.showData();
        } catch (SQLException e) {
            System.out.println("Tidak bisa menambah data. Error: " + e.getMessage());
        }
    }

    private void updateData() {
        try {
            Scanner scan = new Scanner(System.in);

            System.out.println("Masukan ID Siswa yang akan di ubah: ");
            int id = Integer.parseInt(scan.nextLine().trim());

            System.out.println("Masukkan Nama Siswa Baru: ");
            String nama = scan.nextLine();

            System.out.println("Masukkan TTL Siswa Baru: ");
            String ttl = scan.nextLine();

            System.out.println("Masukan Jenis Kelamin Siswa Baru: ");
            String jk = scan.nextLine();

            scan.close();

            String query = "UPDATE siswa SET nama=?,ttl=?,jk=? " + "WHERE id_siswa=?";

            PreparedStatement prep = this.db.conn.prepareStatement(query);

            prep.setString(1, nama);
            prep.setString(2, ttl);
            prep.setString(3, jk);
            prep.setInt(4, id);

            this.db.conn.setAutoCommit(false);
            prep.executeUpdate();
            this.db.conn.setAutoCommit(true);

            SiswaModel siswa = new SiswaModel();
            siswa.id_siswa = id;
            this.getAllData(siswa);
        } catch (SQLException e) {
            System.out.println("Gagal mengedit data. Error: " + e.getMessage());
        }
    }

    private void deleteData() {
        try {
            Scanner scan = new Scanner(System.in);

            System.out.println("Masukan ID Siswa yang akan di hapus: ");
            Integer id = scan.nextInt();

            scan.close();

            String query = "DELETE FROM siswa WHERE id_siswa=?";

            PreparedStatement prep = this.db.conn.prepareStatement(query);

            prep.setInt(1, id);

            this.db.conn.setAutoCommit(false);
            prep.executeUpdate();
            this.db.conn.setAutoCommit(true);

            System.out.println("Berhasil menghapus data siswa.");
        } catch (SQLException e) {
            System.out.println("Tidak bisa menghapus data. Error: " + e.getMessage());
        }
    }
}
