import entity.Image;
import entity.URL;
import service.UrlAndImageDaoImpl;

import java.sql.SQLException;
import java.util.*;


public class Main {
    public final static String INFO = "1";
    public final static String EXIT = "2";
    public final static String ADD_URL = "3";
    public final static String SHOW_URLS = "4";
    public final static String SHOW_IMAGES = "5";
    public final static String SHOW_IMAGES_BY_URL = "6";
    public final static String DOWNLOAD_IMAGE = "7";

    public static void main(String[] args) throws SQLException {
        String  action = "";
        do {
            System.out.println("Enter number command>>\n" +
                    "1.info\n2.exit\n3.add Url" +
                    "\n4.show urls\n5.show images" +
                    "\n6.show images by url\n7.download image");
            System.out.print("Choose action ");
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNextLine()){
                action = scanner.nextLine();
                chooseAction(action);
            }
        }while (!action.equals(EXIT));

    }

    private static void chooseAction(String i) throws SQLException {
        UrlAndImageDaoImpl dao = new UrlAndImageDaoImpl();
        dao.createTables();
        try {
            if (i.equalsIgnoreCase(INFO)){
                System.out.println("Info");
            } else if (i.equalsIgnoreCase(ADD_URL)) {
                Scanner line = new Scanner(System.in);
                System.out.print("Введите url: ");
                String url = line.nextLine();
                UrlThread thread = new UrlThread(url);
                thread.start();
            } else if (i.equalsIgnoreCase(SHOW_URLS)){
                List<URL> allURLS = dao.getAllUrls();
                for (URL url : allURLS) {
                    System.out.println(url.toString());
                }
            } else if (i.equalsIgnoreCase(SHOW_IMAGES)){
                List<Image> images = dao.getAllImages();
                for (Image image : images) {
                    System.out.println(image.toString());
                }
            } else if (i.equalsIgnoreCase(SHOW_IMAGES_BY_URL)){
                Scanner scanner = new Scanner(System.in);
                System.out.print("Введите url: ");
                String url = scanner.nextLine();
                List<Image> images = dao.getImageByUrlName(url);
                for (Image image : images) {
                    System.out.println(image.toString());
                }

            } else if (i.equalsIgnoreCase(DOWNLOAD_IMAGE)){
                Scanner scanner = new Scanner(System.in);
                System.out.print("Введите имя картинки: ");
                String name = scanner.nextLine();
                System.out.print("Введите папку: ");
                String folder = scanner.nextLine();
                dao.downloadImage(name, folder);
            }
            else {
                if (!i.equalsIgnoreCase(EXIT)){
                    System.out.println("Repeat action");
                }
            }
        } catch (InputMismatchException e){
            System.out.println("Choose correct action");
        }
    }
}
