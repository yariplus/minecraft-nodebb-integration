package com.yaricraft.nodebbintegration;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
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
        try {
            commandSender.sendMessage("Registering you on " + NodeBBIntegration.config.getString("FORUMNAME") + "...");
        }catch (Exception e) {
            e.printStackTrace();
            return;
        }

        String urlParameters = "";
        urlParameters += "key=" + NodeBBIntegration.config.getString("KEY") + "&";
        urlParameters += "uuid=" + ((Player)commandSender).getUniqueId().toString() + "&";
        urlParameters += "username=" + commandSender.getName() + "&";
        urlParameters += "password=" + strings[1] + "&";
        urlParameters += "email=" + strings[0];

        byte[] postData       = urlParameters.getBytes( Charset.forName("UTF-8"));
        int    postDataLength = postData.length;

        HttpURLConnection con = null;
        String strURL = NodeBBIntegration.config.getString("URL");
        URL url = null;
        try {
            url = new URL(strURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return;
        }

        try
        {
            con = (HttpURLConnection) url.openConnection();
        }catch (IOException e) {
            e.printStackTrace();
            return;
        }

        if (con == null) return;

        con.setDoOutput( true );
        con.setDoInput ( true );
        con.setInstanceFollowRedirects( false );
        try {
            con.setRequestMethod( "POST" );
        } catch (ProtocolException e) {
            e.printStackTrace();
            return;
        }
        con.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
        con.setRequestProperty( "charset", "utf-8");
        con.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
        con.setUseCaches( false );

        try( DataOutputStream wr = new DataOutputStream( con.getOutputStream())) {
            wr.write( postData );
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        InputStream ins = null;
        try {
            ins = con.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        InputStreamReader isr = new InputStreamReader(ins);
        BufferedReader in = new BufferedReader(isr);

        String inputLine;
        try {
            while ((inputLine = in.readLine()) != null)
            {
                switch (inputLine) {
                    case "SUCCESS":
                        commandSender.sendMessage("Successfully created a new account.");
                        break;
                    case "REREGISTER":
                        commandSender.sendMessage("Successfully registered with your existing forum account.");
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
                        // TEMP
                        // commandSender.sendMessage(inputLine);
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        if (con instanceof HttpsURLConnection) System.out.println(((HttpsURLConnection) con).getCipherSuite());

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
}
