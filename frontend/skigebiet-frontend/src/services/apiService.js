import { notificationService } from './notificationService';

// API Base URL - should be configured based on environment
const API_BASE_URL = 'http://localhost:8080';

// Helper function for API calls
const apiCall = async (endpoint, options = {}) => {
    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            ...options,
            headers: {
                'Content-Type': 'application/json',
                ...options.headers,
            },
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            const errorMessage = errorData.message || response.statusText;
            notificationService.error(`Operation failed: ${errorMessage}`);
            throw new Error(errorMessage);
        }

        // For 204 No Content responses
        if (response.status === 204) {
            return null;
        }

        const data = await response.json();
        
        // Show success message for POST, PUT, DELETE operations
        if (options.method) {
            const action = options.method === 'POST' ? 'created' :
                          options.method === 'PUT' ? 'updated' :
                          options.method === 'DELETE' ? 'deleted' : '';
            if (action) {
                notificationService.success(`Successfully ${action}`);
            }
        }

        return data;
    } catch (error) {
        if (!error.message.includes('Operation failed')) {
            notificationService.error('Network error or server is not responding');
        }
        throw error;
    }
};

// Ski Lift operations
const skiLiftService = {
    getAll: () => apiCall('/api/skilifts'),
    getById: (id) => apiCall(`/api/skilifts/${id}`),
    create: (skiLift) => apiCall('/api/skilifts', {
        method: 'POST',
        body: JSON.stringify(skiLift),
    }),
    update: (id, skiLift) => apiCall(`/api/skilifts/${id}`, {
        method: 'PUT',
        body: JSON.stringify(skiLift),
    }),
    delete: (id) => apiCall(`/api/skilifts/${id}`, {
        method: 'DELETE',
    }),
    getPisten: (id) => apiCall(`/api/skilifts/${id}/pisten`),
};

// Piste operations
const pisteService = {
    getAll: () => apiCall('/api/pisten'),
    getById: (id) => apiCall(`/api/pisten/${id}`),
    create: (piste) => apiCall('/api/pisten', {
        method: 'POST',
        body: JSON.stringify(piste),
    }),
    update: (id, piste) => apiCall(`/api/pisten/${id}`, {
        method: 'PUT',
        body: JSON.stringify(piste),
    }),
    delete: (id) => apiCall(`/api/pisten/${id}`, {
        method: 'DELETE',
    }),
    getBySkiLiftId: (skiLiftId) => apiCall(`/api/pisten/skilift/${skiLiftId}`),
};

// Ticket operations
const ticketService = {
    getAll: () => apiCall('/api/tickets'),
    getById: (id) => apiCall(`/api/tickets/${id}`),
    create: (ticket) => apiCall('/api/tickets', {
        method: 'POST',
        body: JSON.stringify(ticket),
    }),
    update: (id, ticket) => apiCall(`/api/tickets/${id}`, {
        method: 'PUT',
        body: JSON.stringify(ticket),
    }),
    delete: (id) => apiCall(`/api/tickets/${id}`, {
        method: 'DELETE',
    }),
    getByUserId: (userId) => apiCall(`/api/tickets/user/${userId}`),
};

// User operations
const userService = {
    getAll: () => apiCall('/api/users'),
    getById: (id) => apiCall(`/api/users/${id}`),
    getByEmail: (email) => apiCall(`/api/users/email/${email}`),
    create: (user) => apiCall('/api/users', {
        method: 'POST',
        body: JSON.stringify(user),
    }),
    update: (id, user) => apiCall(`/api/users/${id}`, {
        method: 'PUT',
        body: JSON.stringify(user),
    }),
    delete: (id) => apiCall(`/api/users/${id}`, {
        method: 'DELETE',
    }),
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