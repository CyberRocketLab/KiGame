# Readme for AI-Driven Strategy Game Project

## Project Overview

This AI-driven strategy game involves two client-based AIs competing on a shared map to complete a similar task faster than the other. The game is coordinated by a server that facilitates data exchange, acts as a referee, and stores, updates, and evaluates client data. This creates a classic client/server architecture for the game.

### Game Concept

- **Initial Step**: A human initiates a new game on the server.
- **AI Registration**: After client startup, AIs register for the game on the server and exchange map halves, which are combined by the server to form the game map.
- **Gameplay**:
  - Each AI starts from a selected position (their castle) on their half of the map.
  - The objective is to find a hidden treasure on their map half and then locate the opponent's castle.
  - Movement across the map is used to uncover tiles and find the treasure and the opponent's castle.
  - The game is turn-based, with each AI performing one action per turn.
  - The game has a maximum duration of 10 minutes (320 turns).
  - AIs automatically perform actions like picking up treasures and capturing the opponent's castle.

### Game Map

- **Map Creation**: Each AI randomly generates a half of the final game map, combined by the server.
- **Terrain Types**: The map consists of water, meadows, and mountains.
- **Special Locations**: Castles and treasures are placed on meadow tiles.

### Game Mechanics

- **Movement and Actions**:
  - AIs move their figure across the map, revealing tiles.
  - Movement is restricted to horizontal and vertical directions.
  - AIs must avoid water tiles and the edges of the map.
- **Terrain Interaction**:
  - Meadows and mountains have different movement requirements.
  - Mountains provide a strategic advantage by revealing nearby hidden items.

### Game Rules

- **Opponent's Position**: Initially randomized for the first 16 turns to prevent early game advantage.
- **Turn-Based Play**: AIs can only perform one action per turn and cannot skip turns.
- **Time Limit**: Each AI has a maximum of 5 seconds per turn.
- **Victory Conditions**: The first AI to find its treasure and capture the opponent's castle wins.

### Technical Specifications

- **Client/Server Architecture**: Clients communicate with the server for game state updates and actions.
- **Data Handling**: The server manages the game state, including terrain, treasures, and player actions.
