package com.yaricraft.nodebbintegration;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

public class TaskRegister extends BukkitRunnable
{
    public TaskRegister(CommandSender commandSender, String[] strings) {
        this.commandSender = commandSender;
        this.strings = strings;
    }

    CommandSender commandSender;
    String[] strings;

    @Override
    public void run()
    {
        commandSender.sendMessage("Registering you on " + NodeBBIntegration.config.getString("FORUMNAME") + "...");

        try
        {
            String urlParameters = "";
            urlParameters += "key=" + NodeBBIntegration.config.getString("KEY") + "&";
            urlParameters += "uuid=" + ((Player)commandSender).getUniqueId().toString() + "&";
            urlParameters += "username=" + commandSender.getName() + "&";
            urlParameters += "password=" + strings[1] + "&";
            urlParameters += "email=" + strings[0];

            byte[] postData       = urlParameters.getBytes( Charset.forName("UTF-8"));
            int    postDataLength = postData.length;

            String httpsURL = NodeBBIntegration.config.getString("URL");
            URL url = new URL(httpsURL);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

            con.setDoOutput( true );
            con.setDoInput ( true );
            con.setInstanceFollowRedirects( false );
            con.setRequestMethod( "POST" );
            con.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty( "charset", "utf-8");
            con.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
            con.setUseCaches( false );

            try( DataOutputStream wr = new DataOutputStream( con.getOutputStream())) {
                wr.write( postData );
            }

            InputStream ins = con.getInputStream();
            InputStreamReader isr = new InputStreamReader(ins);
            BufferedReader in = new BufferedReader(isr);

            String inputLine;
            while ((inputLine = in.readLine()) != null)
            {
                switch (inputLine) {
                    case "SUCCESS":
                        commandSender.sendMessage("Successfully created a new account.");
                        break;
                    case "RECREATED":
                        commandSender.sendMessage("Account recreated.");
                        break;
                    case "FAILPASS":
                        commandSender.sendMessage("Your password was too short.");
                        break;
                    case "[[error:email-taken]]":
                        commandSender.sendMessage("That email was already taken.");
                        commandSender.sendMessage("Inform an administrator if you believe this is an error.");
                        break;
                    case "FAILEMAIL":
                        commandSender.sendMessage("Your email was invalid.");
                        commandSender.sendMessage("Inform an administrator if you believe this is an error.");
                        break;
                    case "FAILKEY":
                        commandSender.sendMessage("Registration Pass Key was invalid, please inform an administrator.");
                        System.out.println("NodeBB failed the configured pass key: " + NodeBBIntegration.config.getString("KEY"));
                        break;
                    default:
                        commandSender.sendMessage(inputLine);
                        break;
                }
            }

            System.out.println(con.getCipherSuite());

            in.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
