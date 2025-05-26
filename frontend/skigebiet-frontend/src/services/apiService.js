import { notificationService } from './notificationService';

// API Base URL - should be configured based on environment
const API_BASE_URL = 'http://localhost:8080';

// Helper function for API calls
const apiCall = async (endpoint, method = 'GET', body = null) => {
    try {
        const options = {
            method,
            headers: {
                'Content-Type': 'application/json',
            },
        };

        if (body) {
            options.body = JSON.stringify(body);
        }

        const response = await fetch(`${API_BASE_URL}${endpoint}`, options);
        const data = await response.json();

        if (!response.ok) {
            const errorMessage = data.message || 'Operation failed';
            notificationService.error(errorMessage);
            throw new Error(errorMessage);
        }

        // Show success notification for POST, PUT, DELETE operations
        if (method !== 'GET') {
            const action = method === 'POST' ? 'created' : method === 'PUT' ? 'updated' : 'deleted';
            notificationService.success(`Successfully ${action}`);
        }

        return data;
    } catch (error) {
        if (error.message !== 'Operation failed') {
            notificationService.error('Network error or server is not responding');
        }
        throw error;
    }
};

// Ski Lift operations
const skiLiftService = {
    getAll: () => apiCall('/api/skilifts'),
    getById: (id) => apiCall(`/api/skilifts/${id}`),
    create: async (skiLift) => {
        try {
            // Validate ski lift data
            if (!skiLift.name || !skiLift.typ || !skiLift.kapazitaet) {
                throw new Error('Name, type, and capacity are required');
            }

            // Format ski lift data according to API expectations
            const skiLiftData = {
                name: skiLift.name,
                typ: skiLift.typ,
                kapazitaet: parseInt(skiLift.kapazitaet)
            };

            return await apiCall('/api/skilifts', 'POST', skiLiftData);
        } catch (error) {
            notificationService.error(error.message);
            throw error;
        }
    },
    update: (id, skiLift) => apiCall(`/api/skilifts/${id}`, 'PUT', skiLift),
    delete: (id) => apiCall(`/api/skilifts/${id}`, 'DELETE'),
    getPisten: (id) => apiCall(`/api/skilifts/${id}/pisten`),
};

// Piste operations
const pisteService = {
    getAll: () => apiCall('/api/pisten'),
    getById: (id) => apiCall(`/api/pisten/${id}`),
    create: async (piste) => {
        try {
            // Validate piste data
            if (!piste.name || !piste.schwierigkeitsgrad || !piste.laenge || !piste.skiLiftId) {
                throw new Error('Name, difficulty, length, and ski lift are required');
            }

            // Format piste data according to API expectations
            const pisteData = {
                name: piste.name,
                schwierigkeitsgrad: piste.schwierigkeitsgrad,
                laenge: parseFloat(piste.laenge),
                skiLift: {
                    id: parseInt(piste.skiLiftId)
                }
            };

            return await apiCall('/api/pisten', 'POST', pisteData);
        } catch (error) {
            notificationService.error(error.message);
            throw error;
        }
    },
    update: (id, piste) => apiCall(`/api/pisten/${id}`, 'PUT', piste),
    delete: (id) => apiCall(`/api/pisten/${id}`, 'DELETE'),
    getBySkiLiftId: (skiLiftId) => apiCall(`/api/pisten/skilift/${skiLiftId}`)
};

// Ticket operations
const ticketService = {
    getAll: () => apiCall('/api/tickets'),
    getById: (id) => apiCall(`/api/tickets/${id}`),
    create: async (ticket) => {
        try {
            // Validate ticket data
            if (!ticket.userId || !ticket.ticketType) {
                throw new Error('User ID and ticket type are required');
            }

            // Format ticket data according to API expectations
            const ticketData = {
                ticketType: ticket.ticketType,
                date: new Date().toISOString().split('T')[0], // Current date in YYYY-MM-DD format
                validFrom: ticket.ticketType === 'Ganztages' ? '08:00' : '12:00',
                validUntil: '16:00',
                user: {
                    id: parseInt(ticket.userId)
                }
            };

            return await apiCall('/api/tickets', 'POST', ticketData);
        } catch (error) {
            notificationService.error(error.message);
            throw error;
        }
    },
    update: (id, ticket) => apiCall(`/api/tickets/${id}`, 'PUT', ticket),
    delete: (id) => apiCall(`/api/tickets/${id}`, 'DELETE'),
    getByUserId: async (userId) => {
        try {
            const tickets = await apiCall(`/api/tickets/user/${userId}`);
            return Array.isArray(tickets) ? tickets : [];
        } catch (error) {
            console.error('Error fetching tickets for user:', error);
            return [];
        }
    },
    getByPisteId: (pisteId) => apiCall(`/api/tickets/piste/${pisteId}`)
};

// User operations
const userService = {
    getAll: () => apiCall('/api/users'),
    getById: (id) => apiCall(`/api/users/${id}`),
    getByEmail: (email) => apiCall(`/api/users/email/${email}`),
    create: (user) => apiCall('/api/users', 'POST', user),
    update: (id, user) => apiCall(`/api/users/${id}`, 'PUT', user),
    delete: (id) => apiCall(`/api/users/${id}`, 'DELETE'),
    getTickets: (id) => apiCall(`/api/users/${id}/tickets`),
};

// Statistics operations
const statisticsService = {
    getAll: () => apiCall('/api/statistics'),
};

export {
    skiLiftService,
    pisteService,
    ticketService,
    userService,
    statisticsService
}; 