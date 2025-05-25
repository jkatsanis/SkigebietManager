// Local Storage Keys
const STORAGE_KEYS = {
    SKI_LIFTS: 'ski_lifts',
    PISTEN: 'pisten',
    TICKETS: 'tickets',
    USERS: 'users'
};

// Default data
const DEFAULT_DATA = {
    SKI_LIFTS: [
        {
            id: 1,
            name: "Gipfelbahn",
            typ: "Sessellift",
            kapazität: 1200
        },
        {
            id: 2,
            name: "Talbahn",
            typ: "Schlepplift",
            kapazität: 800
        }
    ],
    PISTEN: [
        {
            id: 1,
            name: "Gipfelabfahrt",
            schwierigkeitsgrad: "Schwer",
            laenge: 3.5,
            skiLift: { id: 1 }
        },
        {
            id: 2,
            name: "Talabfahrt",
            schwierigkeitsgrad: "Leicht",
            laenge: 2.0,
            skiLift: { id: 2 }
        }
    ],
    TICKETS: [
        {
            id: 1,
            userId: 1,
            ticketType: "Ganztages",
            date: "2024-03-20",
            validFrom: "08:00",
            validUntil: "16:00"
        },
        {
            id: 2,
            userId: 1,
            ticketType: "Halbtages",
            date: "2024-03-20",
            validFrom: "12:00",
            validUntil: "16:00"
        }
    ],
    USERS: [
        {
            id: 1,
            name: "Max Mustermann",
            email: "max@example.com"
        }
    ]
};

// Helper functions for localStorage
const getItem = (key) => {
    const item = localStorage.getItem(key);
    return item ? JSON.parse(item) : null;
};

const setItem = (key, value) => {
    localStorage.setItem(key, JSON.stringify(value));
};

// Initialize storage with default values if empty
const initializeStorage = () => {
    if (!getItem(STORAGE_KEYS.SKI_LIFTS)) {
        setItem(STORAGE_KEYS.SKI_LIFTS, DEFAULT_DATA.SKI_LIFTS);
    }
    if (!getItem(STORAGE_KEYS.PISTEN)) {
        setItem(STORAGE_KEYS.PISTEN, DEFAULT_DATA.PISTEN);
    }
    if (!getItem(STORAGE_KEYS.TICKETS)) {
        setItem(STORAGE_KEYS.TICKETS, DEFAULT_DATA.TICKETS);
    }
    if (!getItem(STORAGE_KEYS.USERS)) {
        setItem(STORAGE_KEYS.USERS, DEFAULT_DATA.USERS);
    }
};

// Ski Lift operations
const skiLiftService = {
    getAll: () => getItem(STORAGE_KEYS.SKI_LIFTS) || [],
    getById: (id) => {
        const skiLifts = getItem(STORAGE_KEYS.SKI_LIFTS) || [];
        return skiLifts.find(skiLift => skiLift.id === id);
    },
    create: (skiLift) => {
        const skiLifts = getItem(STORAGE_KEYS.SKI_LIFTS) || [];
        const newSkiLift = {
            ...skiLift,
            id: Date.now(), // Generate a unique ID
        };
        skiLifts.push(newSkiLift);
        setItem(STORAGE_KEYS.SKI_LIFTS, skiLifts);
        return newSkiLift;
    }
};

// Piste operations
const pisteService = {
    getAll: () => getItem(STORAGE_KEYS.PISTEN) || [],
    getById: (id) => {
        const pisten = getItem(STORAGE_KEYS.PISTEN) || [];
        return pisten.find(piste => piste.id === id);
    },
    create: (piste) => {
        const pisten = getItem(STORAGE_KEYS.PISTEN) || [];
        const newPiste = {
            ...piste,
            id: Date.now(), // Generate a unique ID
        };
        pisten.push(newPiste);
        setItem(STORAGE_KEYS.PISTEN, pisten);
        return newPiste;
    },
    getBySkiLiftId: (skiLiftId) => {
        const pisten = getItem(STORAGE_KEYS.PISTEN) || [];
        return pisten.filter(piste => piste.skiLift.id === parseInt(skiLiftId));
    }
};

// Ticket operations
const ticketService = {
    getAll: () => getItem(STORAGE_KEYS.TICKETS) || [],
    getByUserId: (userId) => {
        const tickets = getItem(STORAGE_KEYS.TICKETS) || [];
        return tickets.filter(ticket => ticket.userId === parseInt(userId));
    },
    create: (ticket) => {
        const tickets = getItem(STORAGE_KEYS.TICKETS) || [];
        const newTicket = {
            ...ticket,
            id: Date.now(),
            date: new Date().toISOString().split('T')[0],
            validFrom: ticket.ticketType === "Ganztages" ? "08:00" : "12:00",
            validUntil: ticket.ticketType === "Ganztages" ? "16:00" : "16:00"
        };
        tickets.push(newTicket);
        setItem(STORAGE_KEYS.TICKETS, tickets);
        return newTicket;
    }
};

// Statistics operations
const statisticsService = {
    getAll: () => {
        const tickets = getItem(STORAGE_KEYS.TICKETS) || [];
        const users = getItem(STORAGE_KEYS.USERS) || [];
        const pisten = getItem(STORAGE_KEYS.PISTEN) || [];
        const skiLifts = getItem(STORAGE_KEYS.SKI_LIFTS) || [];

        // Calculate tickets by type
        const ticketsByType = {
            Ganztages: tickets.filter(t => t.ticketType === "Ganztages").length,
            Halbtages: tickets.filter(t => t.ticketType === "Halbtages").length
        };

        // Calculate tickets per user
        const ticketsPerUser = users.map(user => {
            const userTickets = tickets.filter(t => t.userId === user.id);
            return {
                userId: user.id,
                userName: user.name,
                totalTickets: userTickets.length,
                ticketTypes: {
                    Ganztages: userTickets.filter(t => t.ticketType === "Ganztages").length,
                    Halbtages: userTickets.filter(t => t.ticketType === "Halbtages").length
                }
            };
        });

        return {
            totalUsers: users.length,
            totalTickets: tickets.length,
            totalPisten: pisten.length,
            totalSkiLifts: skiLifts.length,
            skiLiftCapacity: skiLifts.reduce((total, lift) => total + lift.kapazität, 0),
            ticketsByType: ticketsByType,
            ticketsPerUser: ticketsPerUser
        };
    }
};

// User operations
const userService = {
    getAll: () => getItem(STORAGE_KEYS.USERS) || [],
    getById: (id) => {
        const users = getItem(STORAGE_KEYS.USERS) || [];
        return users.find(user => user.id === parseInt(id));
    },
    create: (user) => {
        const users = getItem(STORAGE_KEYS.USERS) || [];
        const newUser = {
            ...user,
            id: Date.now(), // Generate a unique ID
        };
        users.push(newUser);
        setItem(STORAGE_KEYS.USERS, users);
        return newUser;
    },
    getStatistics: () => {
        const users = getItem(STORAGE_KEYS.USERS) || [];
        const tickets = getItem(STORAGE_KEYS.TICKETS) || [];
        const pisten = getItem(STORAGE_KEYS.PISTEN) || [];
        const skiLifts = getItem(STORAGE_KEYS.SKI_LIFTS) || [];

        return {
            totalUsers: users.length,
            totalTickets: tickets.length,
            totalPisten: pisten.length,
            totalSkiLifts: skiLifts.length,
            ticketsPerUser: users.map(user => ({
                userId: user.id,
                userName: user.name,
                ticketCount: tickets.filter(ticket => ticket.userId === user.id).length
            })),
            pistenByDifficulty: pisten.reduce((acc, piste) => {
                acc[piste.schwierigkeitsgrad] = (acc[piste.schwierigkeitsgrad] || 0) + 1;
                return acc;
            }, {}),
            skiLiftCapacity: skiLifts.reduce((total, lift) => total + lift.kapazität, 0)
        };
    }
};

// Initialize storage when the service is imported
initializeStorage();

export {
    skiLiftService,
    pisteService,
    ticketService,
    userService,
    statisticsService
}; 