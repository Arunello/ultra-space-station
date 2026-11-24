package station.app;

import java.util.Scanner;
import station.control.StationFacade;
import station.core.CommandCenter;
import station.environment.EarthOrbit;

public class Main {
    public static void main(String[] args) {
        CommandCenter center = new CommandCenter(6, 1000, 300, 50);
        center.setEnvironment(new EarthOrbit());
        StationFacade facade = new StationFacade(center);
        System.out.println("Станция на земной орбите. Список команд — help.");
        try (Scanner scanner = new Scanner(System.in)) {
            while (facade.isRunning()) {
                if (System.console() != null) {
                    System.out.print("> ");
                    System.out.flush();
                }
                if (!scanner.hasNextLine()) break;
                String reply = facade.processInput(scanner.nextLine());
                if (!reply.isEmpty()) System.out.println(reply);
            }
        }
    }
}
