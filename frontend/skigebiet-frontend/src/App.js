import React, { useState } from 'react';
import PistenList from './Piste/Pistenlist';
import PisteForm from './Piste/PisteAdder';
import SkiLiftDisplay from './Skilift/SkiLift';
import SkiLiftForm from './Skilift/SkiLiftForm';
import { pisteService, skiLiftService, ticketService, userService, statisticsService } from './services/localStorageService';

function App() {
  const [userTickets, setUserTickets] = useState(null);
  const [skiLiftPisten, setSkiLiftPisten] = useState(null);
  const [pistenCount, setPistenCount] = useState(null);
  const [usersForPiste, setUsersForPiste] = useState(null);
  const [skiLiftsWithDifficulty, setSkiLiftsWithDifficulty] = useState(null);
  const [selectedSkiLiftId, setSelectedSkiLiftId] = useState(1);
  const [selectedPisteId, setSelectedPisteId] = useState(1);
  const [selectedUserId, setSelectedUserId] = useState(1);
  const [newTicket, setNewTicket] = useState({
    userId: 1,
    ticketType: "Ganztages"
  });

  // New Piste Data
  const [newPiste, setNewPiste] = useState({
    name: '',
    schwierigkeitsgrad: '',
    laenge: '',
    skiLiftId: ''
  });

  const [statistics, setStatistics] = useState({
    totalUsers: 0,
    totalTickets: 0,
    totalPisten: 0,
    totalSkiLifts: 0,
    skiLiftCapacity: 0,
    ticketsByType: {},
    ticketsPerUser: []
  });

  const [newUser, setNewUser] = useState({
    name: '',
    email: ''
  });

  // Handle input changes for the Add Piste form
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewPiste(prevState => ({
      ...prevState,
      [name]: value
    }));
  };

  // Fetch tickets for a specific Benutzer (user)
  const fetchUserTickets = (userId) => {
    try {
      const tickets = ticketService.getByUserId(userId);
      const user = userService.getById(userId);
      const ticketsWithDetails = tickets.map(ticket => {
        const piste = pisteService.getById(ticket.pisteId);
        return {
          ...ticket,
          pisteName: piste ? piste.name : 'Unknown Piste',
          userName: user ? user.name : 'Unknown User'
        };
      });
      setUserTickets(ticketsWithDetails);
    } catch (error) {
      console.error('Error fetching user tickets:', error);
    }
  };

  // Fetch all Pisten served by a specific SkiLift
  const fetchSkiLiftPisten = (skiLiftId) => {
    try {
      const pisten = pisteService.getBySkiLiftId(skiLiftId);
      const skiLift = skiLiftService.getById(skiLiftId);
      const pistenWithDetails = pisten.map(piste => ({
        ...piste,
        skiLiftName: skiLift ? skiLift.name : 'Unknown Ski Lift'
      }));
      setSkiLiftPisten(pistenWithDetails);
    } catch (error) {
      console.error('Error fetching Pisten for SkiLift:', error);
    }
  };

  // Fetch total number of Pisten for each SkiLift
  const fetchPistenCount = () => {
    try {
      const pisten = pisteService.getAll();
      const skiLifts = skiLiftService.getAll();
      
      const count = skiLifts.map(skiLift => ({
        skiLiftId: skiLift.id,
        skiLiftName: skiLift.name,
        pistenCount: pisten.filter(piste => piste.skiLift.id === skiLift.id).length
      }));
      
      setPistenCount(count);
    } catch (error) {
      console.error('Error fetching Pisten count:', error);
    }
  };

  // Fetch users who have tickets for a specific Piste
  const fetchUsersForPiste = (pisteId) => {
    try {
      const tickets = ticketService.getByPisteId(pisteId);
      const piste = pisteService.getById(pisteId);
      const usersWithDetails = tickets.map(ticket => {
        const user = userService.getById(ticket.userId);
        return {
          ...ticket,
          userName: user ? user.name : 'Unknown User',
          pisteName: piste ? piste.name : 'Unknown Piste'
        };
      });
      setUsersForPiste(usersWithDetails);
    } catch (error) {
      console.error('Error fetching users for Piste:', error);
    }
  };

  // Fetch SkiLifts with their associated Pisten and difficulty level
  const fetchSkiLiftsWithDifficulty = () => {
    try {
      const skiLifts = skiLiftService.getAll();
      const pisten = pisteService.getAll();
      
      const skiLiftsWithDiff = skiLifts.map(skiLift => {
        const skiLiftPisten = pisten.filter(piste => piste.skiLift.id === skiLift.id);
        return {
          skiLiftId: skiLift.id,
          skiLiftName: skiLift.name,
          skiLiftType: skiLift.typ,
          pisten: skiLiftPisten.map(piste => ({
            name: piste.name,
            difficulty: piste.schwierigkeitsgrad,
            length: piste.laenge
          }))
        };
      });
      
      setSkiLiftsWithDifficulty(skiLiftsWithDiff);
    } catch (error) {
      console.error('Error fetching SkiLifts with difficulty:', error);
    }
  };

  const handleUserInputChange = (e) => {
    const { name, value } = e.target;
    setNewUser(prevState => ({
      ...prevState,
      [name]: value
    }));
  };

  const handleAddUser = (e) => {
    e.preventDefault();
    try {
      userService.create(newUser);
      setNewUser({ name: '', email: '' });
      fetchStatistics();
    } catch (error) {
      console.error('Error adding user:', error);
    }
  };

  const fetchStatistics = () => {
    try {
      const stats = statisticsService.getAll();
      setStatistics(stats);
    } catch (error) {
      console.error('Error fetching statistics:', error);
    }
  };

  const handleTicketInputChange = (e) => {
    const { name, value } = e.target;
    setNewTicket(prevState => ({
      ...prevState,
      [name]: value
    }));
  };

  const handleAddTicket = (e) => {
    e.preventDefault();
    try {
      const ticketToCreate = {
        userId: parseInt(newTicket.userId),
        ticketType: newTicket.ticketType
      };
      ticketService.create(ticketToCreate);
      setNewTicket({ userId: selectedUserId, ticketType: "Ganztages" });
      fetchUserTickets(selectedUserId);
      fetchStatistics();
    } catch (error) {
      console.error('Error adding ticket:', error);
    }
  };

  return (
    <div className="min-h-screen bg-snow-white">
      <header className="bg-ski-blue text-white shadow-lg py-6 mb-8">
        <div className="container mx-auto px-4">
          <h1 className="text-3xl font-bold">Skigebiet Manager</h1>
        </div>
      </header>

      <main className="container mx-auto px-4">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-8 mb-8">
          <div className="bg-white rounded-lg shadow-md p-6">
            <h2 className="text-2xl font-semibold text-ski-blue mb-4">Ski Lifts</h2>
            <SkiLiftDisplay />
            <div className="mt-6">
              <SkiLiftForm />
            </div>
          </div>

          <div className="bg-white rounded-lg shadow-md p-6">
            <h2 className="text-2xl font-semibold text-ski-blue mb-4">Pisten</h2>
            <PistenList />
            <div className="mt-6">
              <PisteForm />
            </div>
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-8 mb-8">
          <div className="bg-white rounded-lg shadow-md p-6">
            <button 
              onClick={() => fetchUserTickets(1)}
              className="w-full bg-ski-blue text-white py-2 px-4 rounded-md hover:bg-blue-800 transition-colors"
            >
              Get Tickets for Benutzer 1
            </button>
            {userTickets && (
              <div className="mt-4">
                <h3 className="text-lg font-semibold text-mountain-gray mb-2">Tickets for Benutzer 1:</h3>
                <pre className="bg-gray-100 p-4 rounded-md overflow-x-auto">{JSON.stringify(userTickets, null, 2)}</pre>
              </div>
            )}
          </div>

          <div className="bg-white rounded-lg shadow-md p-6">
            <div className="mb-4">
              <label className="block text-sm font-medium text-gray-700 mb-2">Select Ski Lift:</label>
              <select
                value={selectedSkiLiftId}
                onChange={(e) => setSelectedSkiLiftId(parseInt(e.target.value))}
                className="w-full px-3 py-2 border border-gray-300 rounded-md"
              >
                {skiLiftService.getAll().map(skiLift => (
                  <option key={skiLift.id} value={skiLift.id}>
                    {skiLift.name}
                  </option>
                ))}
              </select>
            </div>
            <button 
              onClick={() => fetchSkiLiftPisten(selectedSkiLiftId)}
              className="w-full bg-ski-blue text-white py-2 px-4 rounded-md hover:bg-blue-800 transition-colors"
            >
              Get Pisten for Selected Ski Lift
            </button>
            {skiLiftPisten && (
              <div className="mt-4">
                <h3 className="text-lg font-semibold text-mountain-gray mb-2">Pisten for Selected Ski Lift:</h3>
                <pre className="bg-gray-100 p-4 rounded-md overflow-x-auto">{JSON.stringify(skiLiftPisten, null, 2)}</pre>
              </div>
            )}
          </div>

          <div className="bg-white rounded-lg shadow-md p-6">
            <button 
              onClick={fetchPistenCount}
              className="w-full bg-ski-blue text-white py-2 px-4 rounded-md hover:bg-blue-800 transition-colors"
            >
              Get Total Pisten for each SkiLift
            </button>
            {pistenCount && (
              <div className="mt-4">
                <h3 className="text-lg font-semibold text-mountain-gray mb-2">Total Pisten for each SkiLift:</h3>
                <pre className="bg-gray-100 p-4 rounded-md overflow-x-auto">{JSON.stringify(pistenCount, null, 2)}</pre>
              </div>
            )}
          </div>

          

          <div className="bg-white rounded-lg shadow-md p-6">
            <button 
              onClick={fetchSkiLiftsWithDifficulty}
              className="w-full bg-ski-blue text-white py-2 px-4 rounded-md hover:bg-blue-800 transition-colors"
            >
              Get SkiLifts with Difficulty
            </button>
            {skiLiftsWithDifficulty && (
              <div className="mt-4">
                <h3 className="text-lg font-semibold text-mountain-gray mb-2">SkiLifts with Difficulty:</h3>
                <pre className="bg-gray-100 p-4 rounded-md overflow-x-auto">{JSON.stringify(skiLiftsWithDifficulty, null, 2)}</pre>
              </div>
            )}
          </div>
        </div>

        <div className="bg-white rounded-lg shadow-md p-6 mb-8">
          <h2 className="text-2xl font-semibold text-ski-blue mb-4">User Management</h2>
          <form onSubmit={handleAddUser} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">Name:</label>
              <input
                type="text"
                name="name"
                value={newUser.name}
                onChange={handleUserInputChange}
                className="w-full px-3 py-2 border border-gray-300 rounded-md"
                required
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">Email:</label>
              <input
                type="email"
                name="email"
                value={newUser.email}
                onChange={handleUserInputChange}
                className="w-full px-3 py-2 border border-gray-300 rounded-md"
                required
              />
            </div>
            <button
              type="submit"
              className="w-full bg-ski-blue text-white py-2 px-4 rounded-md hover:bg-blue-800 transition-colors"
            >
              Add User
            </button>
          </form>
        </div>

        <div className="bg-white rounded-lg shadow-md p-6 mb-8">
          <h2 className="text-2xl font-semibold text-ski-blue mb-4">Ticket Management</h2>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
            <div>
              <h3 className="text-lg font-semibold text-mountain-gray mb-4">Add New Ticket</h3>
              <form onSubmit={handleAddTicket} className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">Select User:</label>
                  <select
                    name="userId"
                    value={newTicket.userId}
                    onChange={handleTicketInputChange}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md"
                    required
                  >
                    {userService.getAll().map(user => (
                      <option key={user.id} value={user.id}>
                        {user.name}
                      </option>
                    ))}
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">Ticket Type:</label>
                  <select
                    name="ticketType"
                    value={newTicket.ticketType}
                    onChange={handleTicketInputChange}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md"
                    required
                  >
                    <option value="Ganztages">Ganztages (08:00 - 16:00)</option>
                    <option value="Halbtages">Halbtages (12:00 - 16:00)</option>
                  </select>
                </div>
                <button
                  type="submit"
                  className="w-full bg-ski-blue text-white py-2 px-4 rounded-md hover:bg-blue-800 transition-colors"
                >
                  Add Ticket
                </button>
              </form>
            </div>

            <div>
              <h3 className="text-lg font-semibold text-mountain-gray mb-4">View User Tickets</h3>
              <div className="mb-4">
                <label className="block text-sm font-medium text-gray-700 mb-2">Select User:</label>
                <select
                  value={selectedUserId}
                  onChange={(e) => {
                    const userId = parseInt(e.target.value);
                    setSelectedUserId(userId);
                    setNewTicket(prev => ({ ...prev, userId }));
                    fetchUserTickets(userId);
                  }}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md"
                >
                  {userService.getAll().map(user => (
                    <option key={user.id} value={user.id}>
                      {user.name}
                    </option>
                  ))}
                </select>
              </div>
              {userTickets && (
                <div className="mt-4">
                  <h4 className="text-md font-semibold text-mountain-gray mb-2">Tickets:</h4>
                  <div className="bg-gray-50 p-4 rounded-lg">
                    {userTickets.length > 0 ? (
                      <ul className="space-y-2">
                        {userTickets.map(ticket => (
                          <li key={ticket.id} className="flex justify-between items-center">
                            <div>
                              <span className="font-medium">{ticket.ticketType}</span>
                              <span className="text-sm text-gray-500 ml-2">
                                ({ticket.validFrom} - {ticket.validUntil})
                              </span>
                            </div>
                            <span className="text-sm text-gray-500">{ticket.date}</span>
                          </li>
                        ))}
                      </ul>
                    ) : (
                      <p className="text-gray-500">No tickets found for this user.</p>
                    )}
                  </div>
                </div>
              )}
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow-md p-6 mb-8">
          <div className="flex justify-between items-center mb-4">
            <h2 className="text-2xl font-semibold text-ski-blue">Statistics</h2>
            <button
              onClick={fetchStatistics}
              className="bg-ski-blue text-white py-2 px-4 rounded-md hover:bg-blue-800 transition-colors"
            >
              Refresh Statistics
            </button>
          </div>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            <div className="bg-gray-50 p-4 rounded-lg">
              <h3 className="text-lg font-semibold text-mountain-gray mb-2">Overview</h3>
              <ul className="space-y-2">
                <li>Total Users: {statistics.totalUsers}</li>
                <li>Total Tickets: {statistics.totalTickets}</li>
                <li>Total Pisten: {statistics.totalPisten}</li>
                <li>Total Ski Lifts: {statistics.totalSkiLifts}</li>
                <li>Total Ski Lift Capacity: {statistics.skiLiftCapacity}</li>
              </ul>
            </div>

            <div className="bg-gray-50 p-4 rounded-lg">
              <h3 className="text-lg font-semibold text-mountain-gray mb-2">Tickets by Type</h3>
              <ul className="space-y-2">
                <li className="flex justify-between items-center">
                  <span className="font-medium">Ganztages</span>
                  <span className="bg-ski-blue text-white px-2 py-1 rounded-md text-sm">
                    {statistics.ticketsByType.Ganztages} tickets
                  </span>
                </li>
                <li className="flex justify-between items-center">
                  <span className="font-medium">Halbtages</span>
                  <span className="bg-ski-blue text-white px-2 py-1 rounded-md text-sm">
                    {statistics.ticketsByType.Halbtages} tickets
                  </span>
                </li>
              </ul>
            </div>

            <div className="bg-gray-50 p-4 rounded-lg">
              <h3 className="text-lg font-semibold text-mountain-gray mb-2">Tickets per User</h3>
              <ul className="space-y-4">
                {statistics.ticketsPerUser.map(user => (
                  <li key={user.userId} className="border-b border-gray-200 pb-2 last:border-0">
                    <div className="font-medium text-ski-blue">{user.userName}</div>
                    <div className="text-sm text-gray-600 mt-1">
                      <div className="flex justify-between items-center mb-1">
                        <span>Total Tickets:</span>
                        <span className="bg-ski-blue text-white px-2 py-1 rounded-md text-sm">
                          {user.totalTickets}
                        </span>
                      </div>
                      <div className="flex justify-between items-center text-sm">
                        <span>Ganztages:</span>
                        <span className="text-gray-600">{user.ticketTypes.Ganztages}</span>
                      </div>
                      <div className="flex justify-between items-center text-sm">
                        <span>Halbtages:</span>
                        <span className="text-gray-600">{user.ticketTypes.Halbtages}</span>
                      </div>
                    </div>
                  </li>
                ))}
                {statistics.ticketsPerUser.length === 0 && (
                  <li className="text-gray-500">No users found</li>
                )}
              </ul>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
}

export default App;
