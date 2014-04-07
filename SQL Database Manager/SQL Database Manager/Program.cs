using System;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.IO;
using System.Collections.Generic;
using System.Data;
using MySql.Data.MySqlClient;
namespace SQL_Database_Manager
{
    internal class Program
    {
        internal static StreamWriter File =
            new StreamWriter ("./logs/SQLiteServer_" + DateTime.Now.ToString ("yyyy-MM-d_HH_mm_ss") + ".log");

        private const int PortNum = 80;
        private static readonly IPAddress IP = IPAddress.Parse ("0.0.0.0");

        private static void Main ( )
        {
            var listener = new TcpListener (IP, PortNum);
            try
            {
                listener.Start ();
                Console.WriteLine ("Waiting for connection.");
                for (;;)
                {
                    var handler = listener.AcceptTcpClient ();
                    Console.WriteLine ("{0} | Info | Client Connected", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss"));
                    var client = new ClientHandler (handler);
                    ThreadPool.QueueUserWorkItem (client.Process);
                }
            }
            catch (Exception e)
            {
                Console.WriteLine (e.ToString ());
                File.WriteLine ("{0} | Error | {1} ", DateTime.Now.ToString ("yyyy-MM-d HH:mm:ss"), e);
                File.Flush ();
            }
        }
    }
}
