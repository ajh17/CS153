import wci.frontend.PclParser;

public class Pcl
{
    public static void main(String[] args)
    {
        try {
            PclParser.main(args);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
