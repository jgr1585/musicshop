namespace FHVTeamD.MusicshopAPI.CLI
{
    /*
        inspired by https://docs.microsoft.com/en-us/dotnet/csharp/tutorials/console-webapiclient
        "Tutorial: Make HTTP requests in a .NET console app using C#"
    */
    class ApiHttpRequest
    {
        private static readonly HttpClient client = new HttpClient();
        private APIEndpoint _apiEndpoint;
        private string _paramsQueryStr;

        public ApiHttpRequest(APIEndpoint apiEndpoint, Dictionary<string, string> queryParams) {
            string paramsQueryStr = "";
            if (queryParams.Count > 0)
                paramsQueryStr += "?" + String.Join("&", queryParams.Select(p => p.Key +"="+ p.Value));
            _apiEndpoint = apiEndpoint;
            _paramsQueryStr = paramsQueryStr;
        
            // client.DefaultRequestHeaders.Accept.Clear();
            // client.DefaultRequestHeaders.Add("Accept", "application/json");
            client.DefaultRequestHeaders.Add("User-Agent", ".NET Musicshop API CLI Client");
        }

        public async Task<string> send() {
            string requestUri = _apiEndpoint.ApiEndpointUri() + _paramsQueryStr;
            return await client.GetStringAsync(requestUri);
        }
    }
}