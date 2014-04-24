namespace SQL_Database_Manager
{
    public class Rootobject 
    {
        public Device [] Device      { get; set; }
    }

    public class Device     
    {
        public string   DeviceId     { get; set; }
        public App      [] Apps      { get; set; }
        public Location [] Locations { get; set; }
        public Contact  [] Contacts  { get; set; }
    }

    public class App        
    {
        public string Title          { get; set; }
        public string PackageName    { get; set; }
        public string Visible        { get; set; }
    }

    public class Location   
    {
        public string PosId          { get; set; }
        public string Longitude      { get; set; }
        public string Latitude       { get; set; }
    }

    public class Contact    
    {
        public string ContactId      { get; set; }
        public string SurName        { get; set; }
        public string Name           { get; set; }
        public string Number         { get; set; }
        public string TxtAmount      { get; set; }
        public string TxtMax         { get; set; }
        public string CallAmount     { get; set; }
        public string CallMax        { get; set; }
    }
}


