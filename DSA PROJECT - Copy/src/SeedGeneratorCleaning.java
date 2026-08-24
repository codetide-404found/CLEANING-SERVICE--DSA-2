import java.io.File;
import java.io.PrintWriter;
import java.util.Random;

public class SeedGeneratorCleaning {

    public static void main(String[] args) throws Exception {

        String outDir = (args.length >= 1) ? args[0] : "seeds";
        new File(outDir).mkdirs();

        /*
         * ==========================================
         * LOCATIONS
         * ==========================================
         */

        String[] locations = {
                "UGCS",
                "Balme Library",
                "Great Hall",
                "Legon Hall",
                "Commonwealth Hall",
                "Akuafo Hall",
                "Mensah Sarbah Hall",
                "Volta Hall",
                "University Hospital",
                "Night Market",
                "JQB",
                "Pentagon Hostel",
                "Hilla Limann Hall",
                "Banking Square",
                "School of Engineering Sciences",
                "Athletic Oval",
                "Department of Statistics",
                "Department of Mathematics",
                "Business School",
                "University Guest Centre",
                "Diaspora Halls",
                "Elizabeth Sey Hall",
                "Institute of African Studies",
                "Noguchi Memorial Institute",
                "School of Law",
                "School of Public Health",
                "School of Pharmacy",
                "School of Nursing",
                "Graduate School",
                "Institute of Statistical Social and Economic Research",
                "Central Cafeteria",
                "University Basic School",
                "UG Stadium",
                "Sports Directorate",
                "Fire Station",
                "Campus Security Office",
                "Physical Development Office",
                "University Clinic",
                "Main Gate",
                "West Gate",
                "East Gate",
                "Volta River Authority Hostel",
                "Bush Canteen",
                "Engineering Block",
                "Computer Systems Laboratory",
                "Banking Area",
                "SRC Office",
                "International Programmes Office",
                "School of Agriculture",
                "Botanical Gardens",
                "Medical School",
                "Dental School",
                "College of Health Sciences",
                "Transport Yard",
                "Campus Shuttle Terminal"
        };

        try (PrintWriter pw =
                     new PrintWriter(new File(outDir, "locations.csv"))) {

            pw.println(
                    "locationId,name,area,type,latitude,longitude");

            for (int i = 0; i < locations.length; i++) {

                int id = 1001 + i;

                pw.printf(
                        "%d,%s,University of Ghana,Building,5.%06d,-0.%06d%n",
                        id,
                        locations[i],
                        (id * 37) % 1000000,
                        (id * 91) % 1000000
                );
            }
        }

        /*
         * ==========================================
         * ROADS
         * ==========================================
         */

        try (PrintWriter pw =
                     new PrintWriter(new File(outDir, "roads.csv"))) {

            pw.println(
                    "fromLocationId,toLocationId,distance,travelTime,roadConditionWeight");

            for (int i = 1001; i < 1055; i++) {

                pw.printf(
                        "%d,%d,0.30,180,1.0%n",
                        i,
                        i + 1
                );
            }

            Random rnd = new Random(7);

            for (int k = 0; k < 70; k++) {

                int a = 1001 + rnd.nextInt(55);
                int b = 1001 + rnd.nextInt(55);

                if (a != b) {

                    pw.printf(
                            "%d,%d,0.50,240,1.2%n",
                            a,
                            b
                    );
                }
            }
        }

        /*
         * ==========================================
         * CLEANING REQUESTS
         * ==========================================
         */

        try (PrintWriter pw =
                     new PrintWriter(
                             new File(outDir,
                                     "service_requests.csv"))) {

            pw.println(
                    "requestId,source,destination,category,urgency,timeSubmitted,deadline,status");

            Random rnd = new Random(42);

            for (int i = 1; i <= 320; i++) {

                int loc =
                        1001 + rnd.nextInt(55);

                String requestId =
                        String.format("CLN%04d", i);

                int urgency =
                        1 + rnd.nextInt(5);

                String status;

                if (i % 7 == 0)
                    status = "closed";
                else if (i % 5 == 0)
                    status = "pending";
                else
                    status = "open";

                pw.printf(
                        "%s,%d,%d,Cleaning,%d,2026-08-01T%02d:%02d:00,2026-08-02T%02d:00,%s%n",
                        requestId,
                        loc,
                        loc,
                        urgency,
                        8 + (i % 8),
                        (i * 3) % 60,
                        9 + (i % 10),
                        status
                );
            }
        }

        /*
         * ==========================================
         * RESOURCES
         * ==========================================
         */

        try (PrintWriter pw =
                     new PrintWriter(
                             new File(outDir,
                                     "resources.csv"))) {

            pw.println(
                    "resourceId,type,homeLocation,capacity,availabilityStatus");

            int id = 1;

            for (; id <= 15; id++) {

                pw.printf(
                        "RESC%03d,Cleaner,%d,1,available%n",
                        id,
                        1001 + (id % 55)
                );
            }

            for (; id <= 25; id++) {

                pw.printf(
                        "RESC%03d,CleaningTeam,%d,%d,available%n",
                        id,
                        1001 + (id % 55),
                        3 + (id % 4)
                );
            }

            for (; id <= 30; id++) {

                pw.printf(
                        "RESC%03d,Equipment,%d,%d,%s%n",
                        id,
                        1001 + (id % 55),
                        5 + id,
                        (id % 2 == 0)
                                ? "available"
                                : "unavailable"
                );
            }
        }

        System.out.println(
                "University of Ghana seed data generated in: "
                        + outDir);
    }
}