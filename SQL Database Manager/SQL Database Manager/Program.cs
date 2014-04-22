using System;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.IO;
using System.INI;

namespace SQL_Database_Manager
{
    internal class Program
    {
        internal static StreamWriter File;
        internal static string       Db;

        private  static int          _portNum;
        private  static INIFile      _ini;
        private  static IPAddress    _ip;

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
                    var client  = new ClientHandler (handler);
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

            File = new StreamWriter ("./logs/SQLiteServer_" + DateTime.Now.ToString ("yyyy-MM-d_HH_mm_ss") + ".log");
            if (!System.IO.File.Exists ("./config/config.ini"))
            {
                WriteInfo (String.Format ("{0} | Error | config.ini not found; continuing with default values", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss")));
                _portNum = 8080;
                _ip = IPAddress.Parse ("0.0.0.0");
                Db  = String.Format   ("server={0};user={1};password={2};database={3}", "localhost", "root", "", "ParentalControl");
            }
            else
            {
                _ini = new INIFile ("./config/config.ini");
                try
                {
                    _portNum = int.Parse (_ini.GetValue ("Server", "Port"));
                }
                catch (Exception)
                {
                    _portNum = 8080;
                    WriteInfo (String.Format ("{0} | Error | Error in config file fallback to port 8080 ", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss")));
                }

                try
                {
                    _ip = IPAddress.Parse (_ini.GetValue ("Server", "ListeningIP"));
                }
                catch (Exception)
                {
                    _ip = IPAddress.Parse ("0.0.0.0");
                    WriteInfo (String.Format ("{0} | Error | Error in config file fallback to 0.0.0.0 ", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss")));
                }
                
                try
                {
                    Db = String.Format ("server={0};user={1};password={2};database={3}",
                                        _ini.GetValue ("DataBase", "Address"), 
                                        _ini.GetValue ("DataBase", "User"),
                                        _ini.GetValue ("DataBase", "Password"),
                                        _ini.GetValue ("DataBase", "DatabaseName"));
                }
                catch (Exception)
                {
                    WriteInfo (String.Format ("{0} | Error | Error in config file fallback to default database setting",
                                              DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss")));
                    Db = @"server=localhost;userid=root;password=;database=ParentalControl";
                }
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
