import dto.CFG;
import job.Parser;

public class Main {

    public static void main(String[] args) throws Exception {
        try (CFG cfg = new CFG(args[0])){
            Parser parser = new Parser(cfg);
        } catch (Exception e) {
            throw new Exception("Unable to open file");
        }
    }


}
