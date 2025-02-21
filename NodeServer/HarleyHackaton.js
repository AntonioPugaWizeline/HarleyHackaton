
const express = require('express');
const axios = require('axios');

const app = express();
const port = 3000;

const ORS_API_KEY = "";


const server = app.listen(port, () => {
    console.log(`Server running on http://localhost:${port}`);
});

app.use(express.json());

async function getMotorcycleRoute(start, end) {
    try {
        const response = await axios.get(`https://api.openrouteservice.org/v2/directions/driving-car`, {
            params: {
                api_key: ORS_API_KEY,
                start: `${start.longitude},${start.latitude}`,
                end: `${end.longitude},${end.latitude}`
            }
        });

        return response.data.features[0].geometry;
    } catch (error) {
        console.error("Error fetching motorcycle route:", error.response ? error.response.data : error);
        return null;
    }
}

app.post('/route', async (req, res) => {
    const { initialPosition, finalPosition } = req.body;

    if (!initialPosition || !finalPosition) {
        return res.status(400).json({ message: "Not enough data to calculate a route" });
    }

    const route = await getMotorcycleRoute(initialPosition, finalPosition);

    if (!route) {
        return res.status(500).json({ message: "Error calculating motorcycle route" });
    }

    res.json({
        initialPosition,
        finalPosition,
        motorcycleRoute: route
    });
});