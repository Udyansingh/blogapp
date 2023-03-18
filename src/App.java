import java.io.*;
import java.sql.*;
import java.util.*;

public class App {

    public static void checkUser(String userName, Connection connection) {
        try (Statement stmt = connection.createStatement();) {
            String query = "CREATE TABLE IF NOT EXISTS " + userName
                    + "(blogName varchar(100) PRIMARY KEY,blogData mediumtext)";
            stmt.executeUpdate(query);
            System.out.println("\n\nWelcome " + userName + " !");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void console(Connection connection) throws Exception {

        String userName;
        String query;
        Statement stmt = connection.createStatement();

        try (Scanner in = new Scanner(System.in)) {
            System.out.println("\n+----------------------------------------------------------------+");
            System.out.println("|                      Enter Your UserName                       |");
            System.out.println("+----------------------------------------------------------------+");
            System.out.print("-> ");
            userName = in.nextLine();
            checkUser(userName, connection);
            try {
                int option1 = 0;
                while (option1 != 5) {
                    String path = "";
                    System.out.println("\n+----------------------------------------------------------------+");
                    System.out.println("|                                                                |");
                    System.out.println("|                     WELCOME TO MY BLOG APP                     |");
                    System.out.println("|                                                                |");
                    System.out.println("+----------------------------------------------------------------+");
                    System.out.println("|                                                                |");
                    System.out.println("| -> PLEASE SELECT AN OPTION:                                    |");
                    System.out.println("|                                                                |");
                    System.out.println("| 1. CREATE BLOG                                                 |");
                    System.out.println("|                                                                |");
                    System.out.println("| 2. READ BLOG                                                   |");
                    System.out.println("|                                                                |");
                    System.out.println("| 3. UPDATE BLOG                                                 |");
                    System.out.println("|                                                                |");
                    System.out.println("| 4. DELETE BLOG                                                 |");
                    System.out.println("|                                                                |");
                    System.out.println("| 5. EXIT                                                        |");
                    System.out.println("|                                                                |");
                    System.out.println("+----------------------------------------------------------------+");
                    System.out.print("\n-> ");
                    option1 = in.nextInt();
                    in.nextLine();
                    switch (option1) {
                        case 1:
                            System.out.println("\n< Enter Your Blog Name >\n");
                            System.out.print("-> ");
                            String fileName = in.nextLine();
                            query = "SELECT EXISTS(SELECT * FROM " + userName + " WHERE blogName='" + fileName + "')";
                            stmt = connection.createStatement();
                            ResultSet rs = stmt.executeQuery(query);
                            rs.next();
                            if (rs.getInt(1) == 1) {
                                System.out.println("\nBlog with Name " + fileName + " already exists!");
                                break;
                            }
                            File file = new File(path);
                            do {
                                System.out.println("\n< Enter path to your Blog file >\n");
                                System.out.print("-> ");
                                path = in.nextLine();
                                file = new File(path);
                                if (!file.exists() || file.isDirectory()) {
                                    System.out.println("\nInvalid Path or File Does not Exsist !");
                                }
                            } while (!file.exists());
                            FileReader fr = new FileReader(file);
                            PreparedStatement ps = connection
                                    .prepareStatement("insert into " + userName + " values(?,?)");
                            ps.setString(1, fileName);
                            ps.setCharacterStream(2, fr, file.length());
                            ps.executeUpdate();
                            System.out.println("\nBlog Uploaded...");
                            break;

                        case 2:
                            query = "select exists(select 1 from " + userName + ")";
                            stmt = connection.createStatement();
                            rs = stmt.executeQuery(query);
                            rs.next();
                            if (rs.getInt(1) == 0) {
                                System.out.println("\nNo Blogs Found");
                                break;
                            } else {
                                List<String> blogs = new ArrayList<>();
                                query = "select * from " + userName;
                                rs = stmt.executeQuery(query);
                                while (rs.next()) {
                                    blogs.add(rs.getString(1));
                                }
                                System.out
                                        .println("+----------------------------------------------------------------+");
                                System.out
                                        .println("                            YOUR BLOGS                            ");
                                for (int i = 0; i < blogs.size(); i++) {
                                    System.out.println((i + 1) + " -> " + blogs.get(i));
                                }
                                System.out
                                        .println("+----------------------------------------------------------------+");
                                int optionF = 0;
                                do {
                                    System.out.println("\n< Enter number of the blog you want to READ >");
                                    System.out.print("\n-> ");
                                    if (in.hasNextInt()) {
                                        optionF = in.nextInt();
                                        if (optionF > blogs.size() || optionF < 1) {
                                            System.out.println("\nInvalid Input !");
                                        }
                                    } else {
                                        System.out.println("\nInvalid Input !");
                                        in.next();
                                    }
                                } while (optionF > blogs.size() || optionF < 1);
                                query = "SELECT * FROM " + userName + " WHERE blogName='" + blogs.get(optionF - 1)
                                        + "'";
                                rs = stmt.executeQuery(query);
                                System.out.println("\n");
                                while (rs.next()) {
                                    System.out.println(rs.getString(2));
                                }
                            }
                            break;
                        case 3:
                            query = "select exists(select 1 from " + userName + ")";
                            stmt = connection.createStatement();
                            rs = stmt.executeQuery(query);
                            rs.next();
                            if (rs.getInt(1) == 0) {
                                System.out.println("\nNo Blogs were found");
                                break;
                            } else {
                                List<String> blogs = new ArrayList<>();
                                query = "select * from " + userName;
                                rs = stmt.executeQuery(query);
                                while (rs.next()) {
                                    blogs.add(rs.getString(1));
                                }
                                System.out
                                        .println("+----------------------------------------------------------------+");
                                System.out
                                        .println("                            YOUR BLOGS                            ");
                                for (int i = 0; i < blogs.size(); i++) {
                                    System.out.println((i + 1) + " -> " + blogs.get(i));
                                }
                                System.out
                                        .println("+----------------------------------------------------------------+");
                                int optionF = 0;
                                do {
                                    System.out.println("\n< Enter number of the blog you want to UPDATE >");
                                    System.out.print("\n-> ");
                                    if (in.hasNextInt()) {
                                        optionF = in.nextInt();
                                        in.nextLine();
                                        if (optionF > blogs.size() || optionF < 1) {
                                            System.out.println("\nInvalid Input !");
                                        }
                                    } else {
                                        System.out.println("\nInvalid Input !");
                                        in.nextLine();
                                    }
                                } while (optionF > blogs.size() || optionF < 1);
                                do {
                                    System.out.println("\n< Enter path to your updated Blog file >\n");
                                    System.out.print("-> ");
                                    path = in.nextLine();
                                    file = new File(path);
                                    if (!file.exists() || file.isDirectory()) {
                                        System.out.println("\nInvalid Path or File Does not Exsist !");
                                    }
                                } while (!file.exists());
                                fr = new FileReader(file);
                                ps = connection
                                        .prepareStatement(
                                                "update " + userName + " set blogData=(?) where blogName=(?)");
                                ps.setCharacterStream(1, fr, file.length());
                                ps.setString(2, blogs.get(optionF - 1));
                                ps.executeUpdate();
                                System.out.println("\nBlog Updated...");
                            }
                            break;
                        case 4:
                            query = "select exists(select 1 from " + userName + ")";
                            stmt = connection.createStatement();
                            rs = stmt.executeQuery(query);
                            rs.next();
                            if (rs.getInt(1) == 0) {
                                System.out.println("\nNo Blogs Found");
                                break;
                            } else {
                                List<String> blogs = new ArrayList<>();
                                query = "select * from " + userName;
                                rs = stmt.executeQuery(query);
                                while (rs.next()) {
                                    blogs.add(rs.getString(1));
                                }
                                System.out
                                        .println("+----------------------------------------------------------------+");
                                System.out
                                        .println("                            YOUR BLOGS                            ");
                                for (int i = 0; i < blogs.size(); i++) {
                                    System.out.println((i + 1) + " -> " + blogs.get(i));
                                }
                                System.out
                                        .println("+----------------------------------------------------------------+");
                                int optionF = 0;
                                do {
                                    System.out.println("\n< Enter number of the blog you want to DELETE >");
                                    System.out.print("\n-> ");
                                    if (in.hasNextInt()) {
                                        optionF = in.nextInt();
                                        in.nextLine();
                                        if (optionF > blogs.size() || optionF < 1) {
                                            System.out.println("\nInvalid Input !");
                                        }
                                    } else {
                                        System.out.println("\nInvalid Input !");
                                        in.nextLine();
                                    }
                                } while (optionF > blogs.size() || optionF < 1);
                                ps = connection
                                        .prepareStatement(
                                                "delete from " + userName + " where blogName=(?)");
                                ps.setString(1, blogs.get(optionF - 1));
                                ps.executeUpdate();
                                System.out.println("\nBlog Deleted...");
                            }
                            break;
                        case 5:
                            System.out.println("\nExiting...");
                            break;
                        default:
                            break;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) throws Exception {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/blogapp", "root", "root");
            if (connection != null) {
                System.out.println("Database Connected...");
                console(connection);
            }
        } catch (Exception e) {
            System.out.println("Error Connecting Database...");
            e.printStackTrace();
            return;
        }
    }
}
