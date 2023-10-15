package edu.curtin.bustimetable;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.*;


/**
 * Performs the reading/parsing and writing of the CSV files containing timetable entries.
 */
public class FileIO
{
    /**
     * Loads a bus timetable from a given CSV file.
     */
    public List<TimetableEntry> load(File file) throws IOException, TimetableFormatException
    {
        List<TimetableEntry> entries = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(file)))
        {
            String line;
            while((line = br.readLine()) != null)
            {
                String[] fields = line.split(",");
                if(fields.length == 5)
                {
                    try
                    {
                        String routeId = fields[0];
                        String from = fields[1];
                        String destination = fields[2];
                        LocalTime departureTime = LocalTime.parse(fields[3]);
                        Duration duration = Duration.ofMinutes(Integer.parseInt(fields[4]));
                        
                        entries.add(new TimetableEntry(routeId, from, destination, departureTime, duration));
                    }
                    catch(DateTimeParseException e)
                    {
                        throw new TimetableFormatException(String.format(
                            "Invalid departure time: '%s'", fields[3]), e);
                    }
                    catch(NumberFormatException e)
                    {
                        throw new TimetableFormatException(String.format(
                            "Invalid duration: '%s'", fields[4]), e);
                    }                    
                }
            }
        }
        return entries;
    }
    
    /**
     * Writes a bus timetable to a given CSV file.
     */
    public void save(File file, List<TimetableEntry> entries) throws IOException
    {
        try(PrintWriter pw = new PrintWriter(file))
        {
            for(TimetableEntry entry : entries)
            {
                pw.printf("%s,%s,%s,%s,%d\n",
                    entry.getRouteId().replace(",", ""),
                    entry.getFrom().replace(",", ""),
                    entry.getDestination().replace(",", ""),
                    entry.getDepartureTime().toString(),
                    entry.getDuration().toMinutes());
            }
        }
    }
}
