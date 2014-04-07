using System;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.IO;
using Ini;

namespace SQL_Database_Manager
{
    internal class Program
    {
        internal static StreamWriter File = new StreamWriter ("./logs/SQLiteServer_" + DateTime.Now.ToString ("yyyy-MM-d_HH_mm_ss") + ".log");
        internal static string       Db;

        private static int       _portNum;
        private static IniFile   _ini;
        private static IPAddress _ip;


        private static void Main ( )
        {
            Init ();
            
            var listener = new TcpListener (_ip, _portNum);
            try
            {
                listener.Start ();
                Console.WriteLine ("Waiting for connection.");
                for (;;)
                {
                    var handler = listener.AcceptTcpClient ();
                    WriteInfo (String.Format ("{0} | Info | Client Connected", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss")));
                    var client = new ClientHandler (handler);
                    ThreadPool.QueueUserWorkItem (client.Process);
                }
            }
            catch (Exception e)
            {
                WriteInfo (String.Format ("{0} | Error | {1} ", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss"), e));
            }
        }

        private static void Init ( )
        {
            if (!Directory.Exists ("./config"))
                Directory.CreateDirectory ("./config");
            if (!Directory.Exists ("./logs"))
                Directory.CreateDirectory ("./logs");
            _ini = new IniFile ("./config/config.ini");
            if (!System.IO.File.Exists ("./config/config.ini"))
            {
                _ini.IniWriteValue ("Server"  , "Port"        , "8080");
                _ini.IniWriteValue ("Server"  , "ListeningIP" , "0.0.0.0");

                _ini.IniWriteValue ("DataBase", "Address"     , "localhost");
                _ini.IniWriteValue ("DataBase", "User"        , "root");
                _ini.IniWriteValue ("DataBase", "Password"    , "");
                _ini.IniWriteValue ("DataBase", "DatabaseName", "ParentalControl");
            }
            try
            {
                _ip = IPAddress.Parse (_ini.IniReadValue ("Server", "ListeningIP"));
            }
            catch (Exception)
            {
                _ip = IPAddress.Parse ("0.0.0.0");
               WriteInfo (String.Format ("{0} | Error | Error in config file fallback to 0.0.0.0 ", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss")));
            }
            try
            {
                _portNum = int.Parse (_ini.IniReadValue ("Server", "Port"));
            }
            catch (Exception)
            {
                _portNum = 8080;
                WriteInfo (String.Format ("{0} | Error | Error in config file fallback to port 8080 ", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss")));
            }
            try
            {
                Db = String.Format ("server={0};user={1};password={2};database={3}", _ini.IniReadValue ("DataBase", "Address"), _ini.IniReadValue ("DataBase", "User"),
                                                                                     _ini.IniReadValue ("DataBase", "Password"), _ini.IniReadValue ("DataBase", "DatabaseName"));
            }
            catch (Exception)
            {
                WriteInfo (String.Format ("{0} | Error | Error in config file fallback to default database setting", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss")));
                Db = @"server=localhost;userid=root;password=;database=ParentalControl";
            }
        }

        internal static void WriteInfo (string info)
        {
            Console.WriteLine (info);
            File.WriteLine    (info);
            File.Flush ();
        }
    }
}
