import React, { useState, useEffect } from 'react';
import PistenList from './Piste/Pistenlist';
import PisteForm from './Piste/PisteAdder';
import SkiLiftDisplay from './Skilift/SkiLift';
import SkiLiftForm from './Skilift/SkiLiftForm';
import { pisteService, skiLiftService, ticketService, userService, statisticsService } from './services/apiService';

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
    ticketsByType: {
      Ganztages: 0,
      Halbtages: 0
    },
    ticketsPerUser: []
  });

  const [newUser, setNewUser] = useState({
    name: '',
    email: ''
  });

  const [skiLifts, setSkiLifts] = useState([]);
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Add a new state for ticket loading
  const [ticketLoading, setTicketLoading] = useState(false);

  // Add separate loading states for each section
  const [skiLiftPistenLoading, setSkiLiftPistenLoading] = useState(false);
  const [pistenCountLoading, setPistenCountLoading] = useState(false);
  const [skiLiftsWithDifficultyLoading, setSkiLiftsWithDifficultyLoading] = useState(false);

  useEffect(() => {
    const loadInitialData = async () => {
      try {
        setLoading(true);
        const [skiLiftsData, usersData] = await Promise.all([
          skiLiftService.getAll(),
          userService.getAll()
        ]);
        setSkiLifts(skiLiftsData);
        setUsers(usersData);
      } catch (err) {
        console.error('Error loading initial data:', err);
        setError('Failed to load initial data');
      } finally {
        setLoading(false);
      }
    };

    loadInitialData();
  }, []);

  // Handle input changes for the Add Piste form
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewPiste(prevState => ({
      ...prevState,
      [name]: value
    }));
  };

  // Fetch tickets for a specific Benutzer (user)
  const fetchUserTickets = async (userId) => {
    try {
      setTicketLoading(true);
      setError(null);
      const tickets = await ticketService.getByUserId(userId);
      const user = await userService.getById(userId);
      
      // Format the tickets with user details
      const ticketsWithDetails = tickets.map(ticket => ({
        ...ticket,
        userName: user ? user.name : 'Unknown User',
        date: new Date(ticket.date).toISOString().split('T')[0],
        validFrom: ticket.validFrom || (ticket.ticketType === 'Ganztages' ? '08:00' : '12:00'),
        validUntil: ticket.validUntil || '16:00'
      }));
      
      setUserTickets(ticketsWithDetails);
    } catch (error) {
      console.error('Error fetching user tickets:', error);
      setError('Failed to fetch user tickets');
      setUserTickets([]);
    } finally {
      setTicketLoading(false);
    }
  };

  // Fetch all Pisten served by a specific SkiLift
  const fetchSkiLiftPisten = async (skiLiftId) => {
    try {
      setSkiLiftPistenLoading(true);
      setError(null);
      const [pisten, skiLift] = await Promise.all([
        pisteService.getBySkiLiftId(skiLiftId),
        skiLiftService.getById(skiLiftId)
      ]);
      
      const pistenWithDetails = pisten.map(piste => ({
        ...piste,
        skiLiftName: skiLift ? skiLift.name : 'Unknown Ski Lift'
      }));
      setSkiLiftPisten(pistenWithDetails);
    } catch (error) {
      console.error('Error fetching Pisten for SkiLift:', error);
      setError('Failed to fetch Pisten for Ski Lift');
    } finally {
      setSkiLiftPistenLoading(false);
    }
  };

  // Fetch total number of Pisten for each SkiLift
  const fetchPistenCount = async () => {
    try {
      setPistenCountLoading(true);
      setError(null);
      const [pisten, skiLifts] = await Promise.all([
        pisteService.getAll(),
        skiLiftService.getAll()
      ]);
      
      const count = skiLifts.map(skiLift => ({
        skiLiftId: skiLift.id,
        skiLiftName: skiLift.name,
        pistenCount: pisten.filter(piste => piste.skiLift.id === skiLift.id).length
      }));
      
      setPistenCount(count);
    } catch (error) {
      console.error('Error fetching Pisten count:', error);
      setError('Failed to fetch Pisten count');
    } finally {
      setPistenCountLoading(false);
    }
  };

  // Fetch users who have tickets for a specific Piste
  const fetchUsersForPiste = async (pisteId) => {
    try {
      setLoading(true);
      const [tickets, piste] = await Promise.all([
        ticketService.getByPisteId(pisteId),
        pisteService.getById(pisteId)
      ]);
      
      const usersWithDetails = await Promise.all(tickets.map(async ticket => {
        const user = await userService.getById(ticket.userId);
        return {
          ...ticket,
          userName: user ? user.name : 'Unknown User',
          pisteName: piste ? piste.name : 'Unknown Piste'
        };
      }));
      
      setUsersForPiste(usersWithDetails);
    } catch (error) {
      console.error('Error fetching users for Piste:', error);
      setError('Failed to fetch users for Piste');
    } finally {
      setLoading(false);
    }
  };

  // Fetch SkiLifts with their associated Pisten and difficulty level
  const fetchSkiLiftsWithDifficulty = async () => {
    try {
      setSkiLiftsWithDifficultyLoading(true);
      setError(null);
      const [skiLifts, pisten] = await Promise.all([
        skiLiftService.getAll(),
        pisteService.getAll()
      ]);
      
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
      setError('Failed to fetch SkiLifts with difficulty');
    } finally {
      setSkiLiftsWithDifficultyLoading(false);
    }
  };

  const handleUserInputChange = (e) => {
    const { name, value } = e.target;
    setNewUser(prevState => ({
      ...prevState,
      [name]: value
    }));
  };

  const handleAddUser = async (e) => {
    e.preventDefault();
    try {
      await userService.create(newUser);
      setNewUser({ name: '', email: '' });
      // Refresh users list
      const updatedUsers = await userService.getAll();
      setUsers(updatedUsers);
      fetchStatistics();
    } catch (error) {
      console.error('Error adding user:', error);
    }
  };

  const fetchStatistics = async () => {
    try {
      const stats = await statisticsService.getAll();
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

  const handleAddTicket = async (e) => {
    e.preventDefault();
    try {
      const ticketToCreate = {
        userId: parseInt(newTicket.userId),
        ticketType: newTicket.ticketType
      };

      await ticketService.create(ticketToCreate);
      setNewTicket({ userId: selectedUserId, ticketType: "Ganztages" });
      await fetchUserTickets(selectedUserId);
      await fetchStatistics();
    } catch (error) {
      console.error('Error adding ticket:', error);
      // Error notification is handled by the API service
    }
  };

  const handleAddPiste = async (e) => {
    e.preventDefault();
    try {
        const pisteToCreate = {
            name: newPiste.name,
            schwierigkeitsgrad: newPiste.schwierigkeitsgrad,
            laenge: newPiste.laenge,
            skiLiftId: newPiste.skiLiftId
        };

        await pisteService.create(pisteToCreate);
        setNewPiste({
            name: '',
            schwierigkeitsgrad: '',
            laenge: '',
            skiLiftId: ''
        });
        // Refresh pisten list
        await fetchPistenCount();
        await fetchSkiLiftsWithDifficulty();
    } catch (error) {
        console.error('Error adding piste:', error);
        // Error notification is handled by the API service
    }
  };

  // Add useEffect to load statistics on component mount
  useEffect(() => {
    fetchStatistics();
  }, []);

  if (loading) {
    return (
      <div className="flex justify-center items-center min-h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-ski-blue"></div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex justify-center items-center min-h-screen">
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative" role="alert">
          <strong className="font-bold">Error!</strong>
          <span className="block sm:inline"> {error}</span>
        </div>
      </div>
    );
  }

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
              <SkiLiftForm onSkiLiftAdded={async () => {
                try {
                  const updatedSkiLifts = await skiLiftService.getAll();
                  setSkiLifts(updatedSkiLifts);
                } catch (error) {
                  console.error('Error refreshing ski lifts:', error);
                }
              }} />
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
            <div className="mb-4">
              <label className="block text-sm font-medium text-gray-700 mb-2">Select Ski Lift:</label>
              <select
                value={selectedSkiLiftId}
                onChange={(e) => {
                  e.preventDefault();
                  setSelectedSkiLiftId(parseInt(e.target.value));
                }}
                className="w-full px-3 py-2 border border-gray-300 rounded-md"
              >
                {skiLifts.map(skiLift => (
                  <option key={skiLift.id} value={skiLift.id}>
                    {skiLift.name}
                  </option>
                ))}
              </select>
            </div>
            <button 
              onClick={(e) => {
                e.preventDefault();
                fetchSkiLiftPisten(selectedSkiLiftId);
              }}
              className="w-full bg-ski-blue text-white py-2 px-4 rounded-md hover:bg-blue-800 transition-colors"
            >
              Get Pisten for Selected Ski Lift
            </button>
            <div className="mt-4 h-[300px] overflow-y-auto">
              {skiLiftPistenLoading ? (
                <div className="flex justify-center items-center h-full">
                  <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-ski-blue"></div>
                </div>
              ) : skiLiftPisten && (
                <div>
                  <h3 className="text-lg font-semibold text-mountain-gray mb-2">Pisten for Selected Ski Lift:</h3>
                  <div className="space-y-2">
                    {skiLiftPisten.map(piste => (
                      <div key={piste.id} className="bg-gray-50 p-4 rounded-lg border border-gray-200">
                        <div className="flex justify-between items-center">
                          <h4 className="font-medium text-ski-blue">{piste.name}</h4>
                          <span className={`px-2 py-1 rounded text-sm ${
                            piste.schwierigkeitsgrad === 'Einfach' ? 'bg-green-100 text-green-800' :
                            piste.schwierigkeitsgrad === 'Mittel' ? 'bg-blue-100 text-blue-800' :
                            'bg-red-100 text-red-800'
                          }`}>
                            {piste.schwierigkeitsgrad}
                          </span>
                        </div>
                        <p className="text-gray-600 mt-1">Length: {piste.laenge} meters</p>
                      </div>
                    ))}
                  </div>
                </div>
              )}
            </div>
          </div>

          <div className="bg-white rounded-lg shadow-md p-6">
            <button 
              onClick={(e) => {
                e.preventDefault();
                fetchPistenCount();
              }}
              className="w-full bg-ski-blue text-white py-2 px-4 rounded-md hover:bg-blue-800 transition-colors"
            >
              Get Total Pisten for each SkiLift
            </button>
            <div className="mt-4 h-[300px] overflow-y-auto">
              {pistenCountLoading ? (
                <div className="flex justify-center items-center h-full">
                  <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-ski-blue"></div>
                </div>
              ) : pistenCount && (
                <div>
                  <h3 className="text-lg font-semibold text-mountain-gray mb-2">Total Pisten for each SkiLift:</h3>
                  <div className="space-y-2">
                    {pistenCount.map(item => (
                      <div key={item.skiLiftId} className="bg-gray-50 p-4 rounded-lg border border-gray-200">
                        <div className="flex justify-between items-center">
                          <h4 className="font-medium text-ski-blue">{item.skiLiftName}</h4>
                          <span className="bg-ski-blue text-white px-3 py-1 rounded-full text-sm">
                            {item.pistenCount} Pisten
                          </span>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              )}
            </div>
          </div>

          <div className="bg-white rounded-lg shadow-md p-6">
            <button 
              onClick={(e) => {
                e.preventDefault();
                fetchSkiLiftsWithDifficulty();
              }}
              className="w-full bg-ski-blue text-white py-2 px-4 rounded-md hover:bg-blue-800 transition-colors"
            >
              Get SkiLifts with Difficulty
            </button>
            <div className="mt-4 h-[300px] overflow-y-auto">
              {skiLiftsWithDifficultyLoading ? (
                <div className="flex justify-center items-center h-full">
                  <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-ski-blue"></div>
                </div>
              ) : skiLiftsWithDifficulty && (
                <div>
                  <h3 className="text-lg font-semibold text-mountain-gray mb-2">SkiLifts with Difficulty:</h3>
                  <div className="space-y-4">
                    {skiLiftsWithDifficulty.map(skiLift => (
                      <div key={skiLift.skiLiftId} className="bg-gray-50 p-4 rounded-lg border border-gray-200">
                        <div className="flex justify-between items-center mb-2">
                          <h4 className="font-medium text-ski-blue">{skiLift.skiLiftName}</h4>
                          <span className="text-sm text-gray-600">{skiLift.skiLiftType}</span>
                        </div>
                        <div className="mt-2 space-y-2">
                          {skiLift.pisten.map(piste => (
                            <div key={piste.name} className="pl-4 border-l-2 border-gray-200">
                              <div className="flex justify-between items-center">
                                <span className="text-gray-700">{piste.name}</span>
                                <div className="flex items-center space-x-2">
                                  <span className={`px-2 py-1 rounded text-xs ${
                                    piste.difficulty === 'Einfach' ? 'bg-green-100 text-green-800' :
                                    piste.difficulty === 'Mittel' ? 'bg-blue-100 text-blue-800' :
                                    'bg-red-100 text-red-800'
                                  }`}>
                                    {piste.difficulty}
                                  </span>
                                  <span className="text-xs text-gray-500">{piste.length}m</span>
                                </div>
                              </div>
                            </div>
                          ))}
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              )}
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow-md p-6 mb-8">
          <h2 className="text-2xl font-semibold text-ski-blue mb-4">User Management</h2>
          <form onSubmit={(e) => {
            e.preventDefault();
            handleAddUser(e);
          }} className="space-y-4">
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
              <form onSubmit={(e) => {
                e.preventDefault();
                handleAddTicket(e);
              }} className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">Select User:</label>
                  <select
                    name="userId"
                    value={newTicket.userId}
                    onChange={handleTicketInputChange}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md"
                    required
                  >
                    {users.map(user => (
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
                    e.preventDefault();
                    const userId = parseInt(e.target.value);
                    setSelectedUserId(userId);
                    setNewTicket(prev => ({ ...prev, userId }));
                    fetchUserTickets(userId);
                  }}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md"
                >
                  {users.map(user => (
                    <option key={user.id} value={user.id}>
                      {user.name}
                    </option>
                  ))}
                </select>
              </div>
              <div className="mt-4 min-h-[200px]">
                <h4 className="text-md font-semibold text-mountain-gray mb-2">Tickets:</h4>
                <div className="bg-gray-50 p-4 rounded-lg">
                  {ticketLoading ? (
                    <div className="flex justify-center items-center py-4">
                      <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-ski-blue"></div>
                    </div>
                  ) : error ? (
                    <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative" role="alert">
                      <span className="block sm:inline">{error}</span>
                    </div>
                  ) : userTickets && (
                    <>
                      {userTickets.length > 0 ? (
                        <ul className="space-y-2">
                          {userTickets.map(ticket => (
                            <li key={ticket.id} className="flex justify-between items-center p-2 hover:bg-gray-100 rounded">
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
                    </>
                  )}
                </div>
              </div>
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
                    {statistics.ticketsByType?.Ganztages || 0} tickets
                  </span>
                </li>
                <li className="flex justify-between items-center">
                  <span className="font-medium">Halbtages</span>
                  <span className="bg-ski-blue text-white px-2 py-1 rounded-md text-sm">
                    {statistics.ticketsByType?.Halbtages || 0} tickets
                  </span>
                </li>
              </ul>
            </div>

            <div className="bg-gray-50 p-4 rounded-lg">
              <h3 className="text-lg font-semibold text-mountain-gray mb-2">Tickets per User</h3>
              <ul className="space-y-4">
                {statistics.ticketsPerUser?.map(user => (
                  <li key={user.userId} className="border-b border-gray-200 pb-2 last:border-0">
                    <div className="font-medium text-ski-blue">{user.userName}</div>
                    <div className="text-sm text-gray-600 mt-1">
                      <div className="flex justify-between items-center mb-1">
                        <span>Total Tickets:</span>
                        <span className="bg-ski-blue text-white px-2 py-1 rounded-md text-sm">
                          {user.totalTickets || 0}
                        </span>
                      </div>
                      <div className="flex justify-between items-center text-sm">
                        <span>Ganztages:</span>
                        <span className="text-gray-600">{user.ticketTypes?.Ganztages || 0}</span>
                      </div>
                      <div className="flex justify-between items-center text-sm">
                        <span>Halbtages:</span>
                        <span className="text-gray-600">{user.ticketTypes?.Halbtages || 0}</span>
                      </div>
                    </div>
                  </li>
                ))}
                {(!statistics.ticketsPerUser || statistics.ticketsPerUser.length === 0) && (
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
