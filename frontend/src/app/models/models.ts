export interface Plant {
  id?: number;
  name: string;
  species?: string;
  location?: string;
  image?: string;
  tasks?: Task[];
  logs?: Log[];
}

export interface Task {
  id?: number;
  type: string;
  frequencyDays: number;
  lastDone?: string;
  plantId?: number;
  completed?: boolean;
}

export interface Log {
  id?: number;
  date: string;
  note?: string;
  image?: string;
  plantId?: number;
}

export interface User {
  id?: number;
  username: string;
  email: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
}

export interface GoogleAuthRequest {
  credential: string;
}

export interface AuthResponse {
  token: string;
  user: User;
}
