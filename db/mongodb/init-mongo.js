// =============================================================================
// ERP LITE - MongoDB Initialization Script
// Version: 2.0
// Purpose: Initialize catalog database with product categories and audit logs
// Database: erp_catalog_db
// Collections: catalogs, audit_logs, product_documents
// =============================================================================

// Switch to the ERP catalog database
db = db.getSiblingDB('erp_catalog_db');

// =============================================================================
// COLLECTION: catalogs
// Purpose: Store product categories and other catalog data
// =============================================================================

db.createCollection('catalogs');

db.catalogs.insertMany([
    // Product Categories Catalog
    {
        _id: 'catalog-product-categories',
        catalogType: 'PRODUCT_CATEGORIES',
        name: 'Product Categories',
        description: 'Main product categories for the ERP system',
        active: true,
        items: [
            {
                id: 'cat-electronics',
                code: 'ELECTRONICS',
                value: 'Electronics',
                description: 'Electronic devices and accessories',
                displayOrder: 1,
                metadata: {
                    icon: 'laptop',
                    color: '#2196F3'
                }
            },
            {
                id: 'cat-furniture',
                code: 'FURNITURE',
                value: 'Furniture',
                description: 'Office and home furniture',
                displayOrder: 2,
                metadata: {
                    icon: 'chair',
                    color: '#4CAF50'
                }
            },
            {
                id: 'cat-accessories',
                code: 'ACCESSORIES',
                value: 'Accessories',
                description: 'Various accessories and peripherals',
                displayOrder: 3,
                metadata: {
                    icon: 'backpack',
                    color: '#FF9800'
                }
            },
            {
                id: 'cat-stationery',
                code: 'STATIONERY',
                value: 'Stationery',
                description: 'Office supplies and stationery',
                displayOrder: 4,
                metadata: {
                    icon: 'pencil',
                    color: '#9C27B0'
                }
            }
        ],
        createdAt: new Date('2025-01-01T00:00:00Z'),
        updatedAt: new Date('2025-01-01T00:00:00Z')
    },

    // Order Status Catalog
    {
        _id: 'catalog-order-statuses',
        catalogType: 'ORDER_STATUSES',
        name: 'Order Statuses',
        description: 'Possible statuses for orders',
        active: true,
        items: [
            {
                id: 'status-pending',
                code: 'PENDING',
                value: 'Pending',
                description: 'Order created, awaiting confirmation',
                displayOrder: 1,
                metadata: {
                    color: '#FFC107',
                    nextStatuses: ['CONFIRMED', 'CANCELLED']
                }
            },
            {
                id: 'status-confirmed',
                code: 'CONFIRMED',
                value: 'Confirmed',
                description: 'Order confirmed, ready for shipment',
                displayOrder: 2,
                metadata: {
                    color: '#2196F3',
                    nextStatuses: ['SHIPPED', 'CANCELLED']
                }
            },
            {
                id: 'status-shipped',
                code: 'SHIPPED',
                value: 'Shipped',
                description: 'Order shipped to customer',
                displayOrder: 3,
                metadata: {
                    color: '#FF9800',
                    nextStatuses: ['DELIVERED']
                }
            },
            {
                id: 'status-delivered',
                code: 'DELIVERED',
                value: 'Delivered',
                description: 'Order delivered successfully',
                displayOrder: 4,
                metadata: {
                    color: '#4CAF50',
                    nextStatuses: []
                }
            },
            {
                id: 'status-cancelled',
                code: 'CANCELLED',
                value: 'Cancelled',
                description: 'Order cancelled',
                displayOrder: 5,
                metadata: {
                    color: '#F44336',
                    nextStatuses: []
                }
            }
        ],
        createdAt: new Date('2025-01-01T00:00:00Z'),
        updatedAt: new Date('2025-01-01T00:00:00Z')
    },

    // Payment Methods Catalog
    {
        _id: 'catalog-payment-methods',
        catalogType: 'PAYMENT_METHODS',
        name: 'Payment Methods',
        description: 'Available payment methods',
        active: true,
        items: [
            {
                id: 'pay-credit-card',
                code: 'CREDIT_CARD',
                value: 'Credit Card',
                description: 'Visa, Mastercard, American Express',
                displayOrder: 1,
                metadata: {
                    icon: 'credit-card',
                    fee: 2.9
                }
            },
            {
                id: 'pay-paypal',
                code: 'PAYPAL',
                value: 'PayPal',
                description: 'PayPal account',
                displayOrder: 2,
                metadata: {
                    icon: 'paypal',
                    fee: 3.4
                }
            },
            {
                id: 'pay-bank-transfer',
                code: 'BANK_TRANSFER',
                value: 'Bank Transfer',
                description: 'Direct bank transfer',
                displayOrder: 3,
                metadata: {
                    icon: 'bank',
                    fee: 0
                }
            },
            {
                id: 'pay-cash',
                code: 'CASH',
                value: 'Cash',
                description: 'Cash on delivery',
                displayOrder: 4,
                metadata: {
                    icon: 'money',
                    fee: 0
                }
            }
        ],
        createdAt: new Date('2025-01-01T00:00:00Z'),
        updatedAt: new Date('2025-01-01T00:00:00Z')
    },

    // Shipping Methods Catalog
    {
        _id: 'catalog-shipping-methods',
        catalogType: 'SHIPPING_METHODS',
        name: 'Shipping Methods',
        description: 'Available shipping options',
        active: true,
        items: [
            {
                id: 'ship-standard',
                code: 'STANDARD',
                value: 'Standard Shipping',
                description: '5-7 business days',
                displayOrder: 1,
                metadata: {
                    icon: 'truck',
                    cost: 9.99,
                    estimatedDays: 7
                }
            },
            {
                id: 'ship-express',
                code: 'EXPRESS',
                value: 'Express Shipping',
                description: '2-3 business days',
                displayOrder: 2,
                metadata: {
                    icon: 'rocket',
                    cost: 24.99,
                    estimatedDays: 3
                }
            },
            {
                id: 'ship-overnight',
                code: 'OVERNIGHT',
                value: 'Overnight',
                description: 'Next business day',
                displayOrder: 3,
                metadata: {
                    icon: 'zap',
                    cost: 39.99,
                    estimatedDays: 1
                }
            },
            {
                id: 'ship-pickup',
                code: 'PICKUP',
                value: 'Store Pickup',
                description: 'Pick up at store',
                displayOrder: 4,
                metadata: {
                    icon: 'store',
                    cost: 0,
                    estimatedDays: 0
                }
            }
        ],
        createdAt: new Date('2025-01-01T00:00:00Z'),
        updatedAt: new Date('2025-01-01T00:00:00Z')
    },

    // Countries Catalog
    {
        _id: 'catalog-countries',
        catalogType: 'COUNTRIES',
        name: 'Countries',
        description: 'Supported countries for shipping',
        active: true,
        items: [
            {
                id: 'country-us',
                code: 'US',
                value: 'United States',
                description: 'United States of America',
                displayOrder: 1,
                metadata: {
                    flag: '🇺🇸',
                    currency: 'USD',
                    phonePrefix: '+1'
                }
            },
            {
                id: 'country-mx',
                code: 'MX',
                value: 'Mexico',
                description: 'Mexico',
                displayOrder: 2,
                metadata: {
                    flag: '🇲🇽',
                    currency: 'MXN',
                    phonePrefix: '+52'
                }
            },
            {
                id: 'country-ca',
                code: 'CA',
                value: 'Canada',
                description: 'Canada',
                displayOrder: 3,
                metadata: {
                    flag: '🇨🇦',
                    currency: 'CAD',
                    phonePrefix: '+1'
                }
            },
            {
                id: 'country-uk',
                code: 'UK',
                value: 'United Kingdom',
                description: 'United Kingdom',
                displayOrder: 4,
                metadata: {
                    flag: '🇬🇧',
                    currency: 'GBP',
                    phonePrefix: '+44'
                }
            },
            {
                id: 'country-de',
                code: 'DE',
                value: 'Germany',
                description: 'Germany',
                displayOrder: 5,
                metadata: {
                    flag: '🇩🇪',
                    currency: 'EUR',
                    phonePrefix: '+49'
                }
            }
        ],
        createdAt: new Date('2025-01-01T00:00:00Z'),
        updatedAt: new Date('2025-01-01T00:00:00Z')
    },

    // Currencies Catalog
    {
        _id: 'catalog-currencies',
        catalogType: 'CURRENCIES',
        name: 'Currencies',
        description: 'Supported currencies',
        active: true,
        items: [
            {
                id: 'curr-usd',
                code: 'USD',
                value: 'US Dollar',
                description: 'United States Dollar',
                displayOrder: 1,
                metadata: {
                    symbol: '$',
                    decimalPlaces: 2
                }
            },
            {
                id: 'curr-mxn',
                code: 'MXN',
                value: 'Mexican Peso',
                description: 'Mexican Peso',
                displayOrder: 2,
                metadata: {
                    symbol: '$',
                    decimalPlaces: 2
                }
            },
            {
                id: 'curr-eur',
                code: 'EUR',
                value: 'Euro',
                description: 'Euro',
                displayOrder: 3,
                metadata: {
                    symbol: '€',
                    decimalPlaces: 2
                }
            },
            {
                id: 'curr-gbp',
                code: 'GBP',
                value: 'British Pound',
                description: 'British Pound Sterling',
                displayOrder: 4,
                metadata: {
                    symbol: '£',
                    decimalPlaces: 2
                }
            },
            {
                id: 'curr-cad',
                code: 'CAD',
                value: 'Canadian Dollar',
                description: 'Canadian Dollar',
                displayOrder: 5,
                metadata: {
                    symbol: '$',
                    decimalPlaces: 2
                }
            }
        ],
        createdAt: new Date('2025-01-01T00:00:00Z'),
        updatedAt: new Date('2025-01-01T00:00:00Z')
    }
]);

// =============================================================================
// COLLECTION: product_documents
// Purpose: Denormalized product data for fast queries (CQRS Read Model)
// =============================================================================

db.createCollection('product_documents');

db.product_documents.insertMany([
    {
        _id: '11111111-1111-1111-1111-111111111111',
        sku: 'LAPTOP-001',
        name: 'Laptop Dell XPS 15',
        description: 'High-performance laptop with Intel i7, 16GB RAM, 512GB SSD',
        price: 1499.99,
        currency: 'USD',
        stock: 25,
        categoryId: 'cat-electronics',
        categoryName: 'Electronics',
        imageUrl: null,
        active: true,
        tags: ['laptop', 'dell', 'xps', 'intel', 'portable'],
        specifications: {
            processor: 'Intel Core i7-13700H',
            ram: '16GB DDR5',
            storage: '512GB NVMe SSD',
            display: '15.6" FHD',
            weight: '2.0 kg'
        },
        createdAt: new Date('2025-01-01T00:00:00Z'),
        updatedAt: new Date('2025-01-01T00:00:00Z')
    },
    {
        _id: '22222222-2222-2222-2222-222222222222',
        sku: 'LAPTOP-002',
        name: 'MacBook Pro 14"',
        description: 'Apple M3 Pro chip, 18GB RAM, 512GB SSD',
        price: 1999.99,
        currency: 'USD',
        stock: 15,
        categoryId: 'cat-electronics',
        categoryName: 'Electronics',
        imageUrl: null,
        active: true,
        tags: ['laptop', 'apple', 'macbook', 'm3', 'pro'],
        specifications: {
            processor: 'Apple M3 Pro',
            ram: '18GB Unified Memory',
            storage: '512GB SSD',
            display: '14.2" Liquid Retina XDR',
            weight: '1.6 kg'
        },
        createdAt: new Date('2025-01-01T00:00:00Z'),
        updatedAt: new Date('2025-01-01T00:00:00Z')
    },
    {
        _id: '33333333-3333-3333-3333-333333333333',
        sku: 'DESK-001',
        name: 'Standing Desk Electric',
        description: 'Adjustable height standing desk, 120x60cm',
        price: 499.99,
        currency: 'USD',
        stock: 30,
        categoryId: 'cat-furniture',
        categoryName: 'Furniture',
        imageUrl: null,
        active: true,
        tags: ['desk', 'standing', 'electric', 'adjustable', 'ergonomic'],
        specifications: {
            dimensions: '120x60 cm',
            heightRange: '70-120 cm',
            maxLoad: '80 kg',
            material: 'Wood and Steel',
            color: 'White'
        },
        createdAt: new Date('2025-01-01T00:00:00Z'),
        updatedAt: new Date('2025-01-01T00:00:00Z')
    },
    {
        _id: '44444444-4444-4444-4444-444444444444',
        sku: 'CHAIR-001',
        name: 'Ergonomic Office Chair',
        description: 'Herman Miller Aeron, size B, fully adjustable',
        price: 1299.99,
        currency: 'USD',
        stock: 12,
        categoryId: 'cat-furniture',
        categoryName: 'Furniture',
        imageUrl: null,
        active: true,
        tags: ['chair', 'ergonomic', 'herman-miller', 'aeron', 'office'],
        specifications: {
            brand: 'Herman Miller',
            model: 'Aeron',
            size: 'B (Medium)',
            adjustments: 'Height, Armrests, Lumbar, Tilt',
            warranty: '12 years'
        },
        createdAt: new Date('2025-01-01T00:00:00Z'),
        updatedAt: new Date('2025-01-01T00:00:00Z')
    },
    {
        _id: '55555555-5555-5555-5555-555555555555',
        sku: 'MONITOR-001',
        name: 'Dell UltraSharp 27"',
        description: '4K IPS monitor, USB-C, 99% sRGB',
        price: 599.99,
        currency: 'USD',
        stock: 40,
        categoryId: 'cat-electronics',
        categoryName: 'Electronics',
        imageUrl: null,
        active: true,
        tags: ['monitor', 'dell', 'ultrasharp', '4k', 'usb-c'],
        specifications: {
            size: '27 inches',
            resolution: '3840x2160 (4K)',
            panelType: 'IPS',
            colorGamut: '99% sRGB',
            connectivity: 'HDMI, DisplayPort, USB-C'
        },
        createdAt: new Date('2025-01-01T00:00:00Z'),
        updatedAt: new Date('2025-01-01T00:00:00Z')
    },
    {
        _id: '66666666-6666-6666-6666-666666666666',
        sku: 'KEYBOARD-001',
        name: 'Mechanical Keyboard RGB',
        description: 'Cherry MX Brown switches, aluminum frame',
        price: 149.99,
        currency: 'USD',
        stock: 50,
        categoryId: 'cat-electronics',
        categoryName: 'Electronics',
        imageUrl: null,
        active: true,
        tags: ['keyboard', 'mechanical', 'rgb', 'cherry-mx', 'gaming'],
        specifications: {
            switches: 'Cherry MX Brown',
            backlighting: 'RGB per-key',
            layout: 'Full-size (104 keys)',
            connection: 'USB-C wired',
            material: 'Aluminum frame'
        },
        createdAt: new Date('2025-01-01T00:00:00Z'),
        updatedAt: new Date('2025-01-01T00:00:00Z')
    },
    {
        _id: '77777777-7777-7777-7777-777777777777',
        sku: 'MOUSE-001',
        name: 'Logitech MX Master 3S',
        description: 'Wireless mouse, ergonomic, 8K DPI',
        price: 99.99,
        currency: 'USD',
        stock: 60,
        categoryId: 'cat-electronics',
        categoryName: 'Electronics',
        imageUrl: null,
        active: true,
        tags: ['mouse', 'logitech', 'mx-master', 'wireless', 'ergonomic'],
        specifications: {
            sensor: '8000 DPI',
            connectivity: 'Bluetooth, USB receiver',
            battery: 'Rechargeable, 70 days',
            buttons: '7 programmable buttons',
            weight: '141g'
        },
        createdAt: new Date('2025-01-01T00:00:00Z'),
        updatedAt: new Date('2025-01-01T00:00:00Z')
    },
    {
        _id: '88888888-8888-8888-8888-888888888888',
        sku: 'PRINTER-001',
        name: 'HP LaserJet Pro',
        description: 'Black & white laser printer, duplex, Wi-Fi',
        price: 299.99,
        currency: 'USD',
        stock: 20,
        categoryId: 'cat-electronics',
        categoryName: 'Electronics',
        imageUrl: null,
        active: true,
        tags: ['printer', 'hp', 'laserjet', 'laser', 'duplex'],
        specifications: {
            printSpeed: '40 ppm',
            connectivity: 'Wi-Fi, Ethernet, USB',
            duplexPrinting: 'Automatic',
            paperCapacity: '250 sheets',
            monthlyDutyCycle: '80,000 pages'
        },
        createdAt: new Date('2025-01-01T00:00:00Z'),
        updatedAt: new Date('2025-01-01T00:00:00Z')
    },
    {
        _id: '99999999-9999-9999-9999-999999999999',
        sku: 'TABLET-001',
        name: 'iPad Air 11"',
        description: 'M2 chip, 128GB, Wi-Fi, Space Gray',
        price: 599.99,
        currency: 'USD',
        stock: 35,
        categoryId: 'cat-electronics',
        categoryName: 'Electronics',
        imageUrl: null,
        active: true,
        tags: ['tablet', 'ipad', 'apple', 'm2', 'air'],
        specifications: {
            processor: 'Apple M2',
            storage: '128GB',
            display: '11" Liquid Retina',
            camera: '12MP Wide',
            battery: '10 hours'
        },
        createdAt: new Date('2025-01-01T00:00:00Z'),
        updatedAt: new Date('2025-01-01T00:00:00Z')
    },
    {
        _id: 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
        sku: 'LAMP-001',
        name: 'LED Desk Lamp',
        description: 'Adjustable brightness, USB charging port',
        price: 49.99,
        currency: 'USD',
        stock: 80,
        categoryId: 'cat-furniture',
        categoryName: 'Furniture',
        imageUrl: null,
        active: true,
        tags: ['lamp', 'led', 'desk', 'adjustable', 'usb'],
        specifications: {
            lightSource: 'LED',
            brightness: '3 levels',
            colorTemperature: '3000K - 6000K',
            powerSource: 'AC adapter',
            usbPort: '5V 1A output'
        },
        createdAt: new Date('2025-01-01T00:00:00Z'),
        updatedAt: new Date('2025-01-01T00:00:00Z')
    },
    {
        _id: 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
        sku: 'WEBCAM-001',
        name: 'Logitech C920 HD Pro',
        description: '1080p video, stereo audio, autofocus',
        price: 79.99,
        currency: 'USD',
        stock: 45,
        categoryId: 'cat-electronics',
        categoryName: 'Electronics',
        imageUrl: null,
        active: true,
        tags: ['webcam', 'logitech', 'c920', '1080p', 'streaming'],
        specifications: {
            resolution: '1080p at 30fps',
            fieldOfView: '78 degrees',
            autofocus: 'Yes',
            microphone: 'Dual stereo',
            connection: 'USB-A'
        },
        createdAt: new Date('2025-01-01T00:00:00Z'),
        updatedAt: new Date('2025-01-01T00:00:00Z')
    },
    {
        _id: 'cccccccc-cccc-cccc-cccc-cccccccccccc',
        sku: 'HEADSET-001',
        name: 'Sony WH-1000XM5',
        description: 'Noise cancelling, 30h battery, Bluetooth',
        price: 399.99,
        currency: 'USD',
        stock: 28,
        categoryId: 'cat-electronics',
        categoryName: 'Electronics',
        imageUrl: null,
        active: true,
        tags: ['headset', 'sony', 'noise-cancelling', 'wireless', 'bluetooth'],
        specifications: {
            noiseCancellation: 'Active',
            batteryLife: '30 hours',
            connectivity: 'Bluetooth 5.2',
            weight: '250g',
            foldable: 'Yes'
        },
        createdAt: new Date('2025-01-01T00:00:00Z'),
        updatedAt: new Date('2025-01-01T00:00:00Z')
    },
    {
        _id: 'dddddddd-dddd-dddd-dddd-dddddddddddd',
        sku: 'DOCK-001',
        name: 'CalDigit TS4',
        description: 'Thunderbolt 4 dock, 18 ports, 98W power delivery',
        price: 399.99,
        currency: 'USD',
        stock: 18,
        categoryId: 'cat-electronics',
        categoryName: 'Electronics',
        imageUrl: null,
        active: true,
        tags: ['dock', 'thunderbolt', 'caldigit', 'usb-c', 'hub'],
        specifications: {
            ports: '18 total ports',
            thunderbolt: 'Thunderbolt 4',
            powerDelivery: '98W',
            ethernet: 'Gigabit Ethernet',
            sdCard: 'UHS-II SD card reader'
        },
        createdAt: new Date('2025-01-01T00:00:00Z'),
        updatedAt: new Date('2025-01-01T00:00:00Z')
    },
    {
        _id: 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee',
        sku: 'CABLE-001',
        name: 'USB-C Cable 2m',
        description: 'Thunderbolt 4, 40Gbps, 100W charging',
        price: 29.99,
        currency: 'USD',
        stock: 100,
        categoryId: 'cat-electronics',
        categoryName: 'Electronics',
        imageUrl: null,
        active: true,
        tags: ['cable', 'usb-c', 'thunderbolt', 'charging', 'data'],
        specifications: {
            length: '2 meters',
            standard: 'Thunderbolt 4',
            speed: '40Gbps',
            power: '100W charging',
            certification: 'USB-IF certified'
        },
        createdAt: new Date('2025-01-01T00:00:00Z'),
        updatedAt: new Date('2025-01-01T00:00:00Z')
    },
    {
        _id: 'ffffffff-ffff-ffff-ffff-ffffffffffff',
        sku: 'BACKPACK-001',
        name: 'Laptop Backpack',
        description: 'Water-resistant, fits 15" laptop, USB charging port',
        price: 69.99,
        currency: 'USD',
        stock: 55,
        categoryId: 'cat-accessories',
        categoryName: 'Accessories',
        imageUrl: null,
        active: true,
        tags: ['backpack', 'laptop', 'travel', 'water-resistant', 'usb'],
        specifications: {
            laptopSize: 'Up to 15.6 inches',
            material: 'Water-resistant polyester',
            usbPort: 'External USB charging port',
            capacity: '25 liters',
            pockets: 'Multiple compartments'
        },
        createdAt: new Date('2025-01-01T00:00:00Z'),
        updatedAt: new Date('2025-01-01T00:00:00Z')
    },
    {
        _id: '10101010-1010-1010-1010-101010101010',
        sku: 'ORGANIZER-001',
        name: 'Desktop Organizer',
        description: 'Bamboo wood, 5 compartments, cable management',
        price: 39.99,
        currency: 'USD',
        stock: 70,
        categoryId: 'cat-furniture',
        categoryName: 'Furniture',
        imageUrl: null,
        active: true,
        tags: ['organizer', 'desk', 'bamboo', 'storage', 'cable-management'],
        specifications: {
            material: 'Bamboo wood',
            compartments: '5 sections',
            cableManagement: 'Built-in slots',
            dimensions: '30x20x10 cm',
            finish: 'Natural bamboo'
        },
        createdAt: new Date('2025-01-01T00:00:00Z'),
        updatedAt: new Date('2025-01-01T00:00:00Z')
    },
    {
        _id: '20202020-2020-2020-2020-202020202020',
        sku: 'PLANT-001',
        name: 'Artificial Desk Plant',
        description: 'Small succulent, realistic, no maintenance',
        price: 19.99,
        currency: 'USD',
        stock: 90,
        categoryId: 'cat-accessories',
        categoryName: 'Accessories',
        imageUrl: null,
        active: true,
        tags: ['plant', 'succulent', 'artificial', 'decoration', 'desk'],
        specifications: {
            type: 'Artificial succulent',
            height: '15 cm',
            pot: 'Ceramic pot included',
            maintenance: 'None required',
            realistic: 'Lifelike appearance'
        },
        createdAt: new Date('2025-01-01T00:00:00Z'),
        updatedAt: new Date('2025-01-01T00:00:00Z')
    },
    {
        _id: '30303030-3030-3030-3030-303030303030',
        sku: 'NOTEBOOK-001',
        name: 'Moleskine Classic Notebook',
        description: 'Large, ruled, hardcover, 240 pages',
        price: 24.99,
        currency: 'USD',
        stock: 75,
        categoryId: 'cat-stationery',
        categoryName: 'Stationery',
        imageUrl: null,
        active: true,
        tags: ['notebook', 'moleskine', 'journal', 'writing', 'hardcover'],
        specifications: {
            brand: 'Moleskine',
            size: 'Large (13x21 cm)',
            pages: '240 pages',
            ruling: 'Ruled',
            cover: 'Hardcover'
        },
        createdAt: new Date('2025-01-01T00:00:00Z'),
        updatedAt: new Date('2025-01-01T00:00:00Z')
    },
    {
        _id: '40404040-4040-4040-4040-404040404040',
        sku: 'PEN-001',
        name: 'Pilot G2 Gel Pens Pack',
        description: 'Pack of 12, 0.7mm, black ink',
        price: 14.99,
        currency: 'USD',
        stock: 120,
        categoryId: 'cat-stationery',
        categoryName: 'Stationery',
        imageUrl: null,
        active: true,
        tags: ['pen', 'pilot', 'gel', 'writing', 'office'],
        specifications: {
            brand: 'Pilot',
            type: 'Gel pen',
            pointSize: '0.7mm',
            inkColor: 'Black',
            quantity: '12 pens'
        },
        createdAt: new Date('2025-01-01T00:00:00Z'),
        updatedAt: new Date('2025-01-01T00:00:00Z')
    },
    {
        _id: '50505050-5050-5050-5050-505050505050',
        sku: 'WHITEBOARD-001',
        name: 'Magnetic Whiteboard',
        description: '90x60cm, aluminum frame, includes markers',
        price: 89.99,
        currency: 'USD',
        stock: 22,
        categoryId: 'cat-furniture',
        categoryName: 'Furniture',
        imageUrl: null,
        active: true,
        tags: ['whiteboard', 'magnetic', 'office', 'presentation', 'wall'],
        specifications: {
            dimensions: '90x60 cm',
            surface: 'Magnetic dry-erase',
            frame: 'Aluminum',
            included: '3 markers, eraser',
            mounting: 'Wall-mountable'
        },
        createdAt: new Date('2025-01-01T00:00:00Z'),
        updatedAt: new Date('2025-01-01T00:00:00Z')
    }
]);

// =============================================================================
// COLLECTION: audit_logs
// Purpose: Store audit trails for @Auditable annotated methods (AOP)
// =============================================================================

db.createCollection('audit_logs');

// Create indexes for efficient querying
db.audit_logs.createIndex({timestamp: -1});
db.audit_logs.createIndex({className: 1, methodName: 1});
db.audit_logs.createIndex({userId: 1});
db.audit_logs.createIndex({success: 1});

// Insert sample audit log
db.audit_logs.insertOne({
    _id: new ObjectId(),
    className: 'CreateOrderUseCase',
    methodName: 'execute',
    userId: 'admin',
    timestamp: new Date(),
    executionTimeMs: 127,
    success: true,
    errorMessage: null,
    ipAddress: '127.0.0.1',
    endpoint: '/api/v1/orders'
});

// Create indexes for catalogs
db.catalogs.createIndex({catalogType: 1});
db.catalogs.createIndex({active: 1});
db.catalogs.createIndex({'items.code': 1});

// Create indexes for product_documents (CQRS Read Model)
db.product_documents.createIndex({sku: 1}, {unique: true});
db.product_documents.createIndex({categoryId: 1});
db.product_documents.createIndex({active: 1});
db.product_documents.createIndex({tags: 1});
db.product_documents.createIndex({name: 'text', description: 'text'});
db.product_documents.createIndex({price: 1});
db.product_documents.createIndex({stock: 1});

// Display initialization summary
print('\n=============================================================================');
print('ERP LITE - MongoDB Initialization Complete');
print('=============================================================================');
print('Database: erp_catalog_db');
print('Collections created:');
print('  - catalogs (' + db.catalogs.countDocuments() + ' documents)');
print('  - product_documents (' + db.product_documents.countDocuments() + ' documents)');
print('  - audit_logs (' + db.audit_logs.countDocuments() + ' documents)');
print('=============================================================================\n');